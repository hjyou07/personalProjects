#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <unistd.h>
#include <arpa/inet.h>

#include "QueryProtocol.h"

char *port_string = "1500";
unsigned short int port;
char *ip = "127.0.0.1";

#define BUFFER_SIZE 1000
char resp[BUFFER_SIZE];
int num_resp;
int *num_response = &num_resp;

void SendMessage(char *msg, int sock_fd) {
  write(sock_fd, msg, strlen(msg));
}

char *ReadResponse(int sock_fd) {
  int len = read(sock_fd, resp, sizeof(resp) - 1);
  resp[len] = '\0';
  return resp;
}

int *ReadNumberResponse(int sock_fd) {
  int len = read(sock_fd, num_response, sizeof(num_response));
  if (len != 4) {
    // printf("received %d bytes. Should be: 4\n", len);
    num_resp = 0;
  }
  return num_response;
}

void RunQuery(char *query) {
  int s;
  struct addrinfo hints, *result;

  memset(&hints, 0, sizeof(struct addrinfo));
  hints.ai_family = AF_INET;
  hints.ai_socktype = SOCK_STREAM;

  // Find the address
  s = getaddrinfo(ip, port_string, &hints, &result);
  if (s != 0) {
    fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(s));
  }
  // Create the socket
  int sock_fd = socket(AF_INET, SOCK_STREAM, 0);
  // Connect to the server ->
  // I'm not checking for -1 bc that's already done in CheckIPAdcress()
  connect(sock_fd, result->ai_addr, result->ai_addrlen);

  // Do the query-protocol
  // 1. Client recieves ACK
  char *connect_res = ReadResponse(sock_fd);
  if (CheckAck(connect_res) == -1) {
    printf("ACK not received from the server");
    exit(1);
  }
  // 2. Client sends query
  SendMessage(query, sock_fd);
  // 3. Client reads int describing number of results
  int *num_result = ReadNumberResponse(sock_fd);
  printf("Number of matched entries: %d\n", *num_result);
  if (num_resp == 0) {
    printf("SORRY! There is no movies matching your query\n");
  } else {
  // 4. Client sends ACK
  SendAck(sock_fd);
  // 5. For each response:
  //  5.1. Client reads response from server as a string
  //  5.2. Client sends ACK to server
  for (int i = 0; i < *num_result; i++) {
    char *movies = ReadResponse(sock_fd);
    printf("%s\n", movies);
    SendAck(sock_fd);
  }
  // 6. Client reads GOODBYE from server
  char *goodbye_resp = ReadResponse(sock_fd);
  if (CheckGoodbye(goodbye_resp) == -1) {
    printf("GOODBYE not received from the server");
    exit(1);
  }
  }
  // Close the connection
  freeaddrinfo(result);
  result = NULL;
  close(sock_fd);
}

void RunPrompt() {
  char input[BUFFER_SIZE];

  while (1) {
    printf("Enter a term to search for, or q to quit: ");
    scanf("%s", input);
    while (strlen(input) > 100) {
      printf("term too long, limit to 100 characters");
      scanf("%s", input);
    }
    printf("input was: %s\n", input);

    if (strlen(input) == 1) {
      if (input[0] == 'q') {
        printf("Thanks for playing! \n");
        return;
      }
    }
    printf("\n\n");
    RunQuery(input);
  }
}

// This function connects to the given IP/port to ensure
// that it is up and running, before accepting queries from users.
// Returns 0 if can't connect; 1 if can.
int CheckIpAddress(char *ip, char *port) {
  // Connect to the server
  int s;
  struct addrinfo hints, *result;

  memset(&hints, 0, sizeof(struct addrinfo));
  hints.ai_family = AF_INET;
  hints.ai_socktype = SOCK_STREAM;

  // Find the address
  s = getaddrinfo(ip, port_string, &hints, &result);
  if (s != 0) {
    fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(s));
  }
  // Create the socket
  int sock_fd = socket(AF_INET, SOCK_STREAM, 0);
  // Connect to the server
  if (connect(sock_fd, result->ai_addr, result->ai_addrlen) == -1) {
    perror("Connection failed\n");
    return 0;
  } else {
    printf("Connected\n");
    // Listen for an ACK
    // -> The term is confusing, I don't think I need to call listen().
    char *response = ReadResponse(sock_fd);
    if (CheckAck(response) == -1) {
      printf("ACK not received from the server");
      return 0;
    }
    // Send a goodbye
    SendGoodbye(sock_fd);
    // Close the connection
    freeaddrinfo(result);
    result = NULL;
    close(sock_fd);
    return 1;
  }
}

int main(int argc, char **argv) {
  if (argc != 3) {
    printf("Incorrect number of arguments. \n");
    printf("Correct usage: ./queryclient [IP] [port]\n");
  } else {
    ip = argv[1];
    port_string = argv[2];
  }

  if (CheckIpAddress(ip, port_string)) {
    RunPrompt();
  }
  return 0;
}
