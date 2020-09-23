#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <signal.h>

#include "DirectoryParser_MT.h"
#include "QueryProtocol.h"
#include "MovieSet.h"
#include "MovieIndex.h"
#include "MovieTitleIndex.h"
#include "DocIdMap.h"
#include "Hashtable.h"
#include "QueryProcessor.h"
#include "FileParser.h"
#include "FileCrawler.h"
#include "DirectoryParser.h"
#include "Util.h"

DocIdMap docs;
MovieTitleIndex docIndex;

#define BUFFER_SIZE 1000
char resp[BUFFER_SIZE];
#define SEARCH_RESULT_LENGTH 1500
char movieSearchResult[SEARCH_RESULT_LENGTH];

struct addrinfo hints, *result;

int Cleanup();

void sigint_handler(int sig) {
  write(0, "Exit signal sent. Cleaning up...\n", 34);
  Cleanup();
  freeaddrinfo(result);
  exit(0);
}

void SendMessage(char *msg, int sock_fd) {
  write(sock_fd, msg, strlen(msg));
}

void SendNumber(int num, int sock_fd) {
  write(sock_fd, &num, sizeof(num));
}

char *ReadResponse(int sock_fd) {
  int len = read(sock_fd, resp, sizeof(resp) - 1);
  resp[len] = '\0';
  return resp;
}

int HandleClient(int sock_fd) {
  // Step 5: Accept connection
  printf("Waiting for connection...\n");
  int client_fd = accept(sock_fd, NULL, NULL);
  if (client_fd == -1) {
    perror("Connection not accepted");
    exit(1);
  }
  printf("Connection made: client_fd: %d\n\n\n", client_fd);
  // Step 6: Read, then write if you want
  // 6.1. Send ACK
  SendAck(client_fd);
  // 6.2. Listen for query
  char *query = ReadResponse(client_fd);
  // If query is GOODBYE close connection
  if (strcmp(query, "GOODBYE") == 0) {
    close(client_fd);  // make sure to close client_fd not sock_fd!
    return 0;
  }

  // 6.3. Run query and get responses (use NumResultsInIter)
  // seg fault here if I search for the term that doesn't exist in the docIndex
  if (GetDocumentSet(docIndex, query) == NULL) {
    SendMessage("SORRY! There is no movies matching your query\n", client_fd);
    printf("SORRY! There is no movies matching your query\n");
    return 0;
  }
  SearchResultIter sr_iter = FindMovies(docIndex, query);
  SearchResult sr = (SearchResult)malloc(sizeof(*sr));
  if (sr == NULL) {
    perror("Couldn't malloc SearchResult in HandleClient()\n");
    exit(1);
  }

  int num_results = NumResultsInIter(sr_iter);
  printf("Number of responses: %d\n", num_results);

  // 6.4. Send number of responses
  SendNumber(num_results, client_fd);
  // 6.5. Wait for ACK
  char *num_res_ack = ReadResponse(client_fd);
  if (CheckAck(num_res_ack) == -1) {
    perror("ACK not received from the client");
    exit(1);
  }
  // Step 7: For each response
  // 7.1. Send response
  // 7.2. Wait for ACK
  int flag = 1;
  while (flag) {
    if (SearchResultGet(sr_iter, sr) == 1) {
      perror("Couldn't get the search result");
      exit(1);
    }
    CopyRowFromFile(sr, docs, movieSearchResult);
    // 7.1
    SendMessage(movieSearchResult, client_fd);
    // 7.2
    if (CheckAck(ReadResponse(client_fd))) {
      perror("ACK not received from the client");
      exit(1);
    }
    if (SearchResultIterHasMore(sr_iter)) {
      SearchResultNext(sr_iter);
    } else {
      printf("End of the search result\n");
      flag = 0;
    }
  }
  // Cleanup
  printf("Cleaning up...\n");
  DestroySearchResultIter(sr_iter);
  free(sr);
  // Step 8: Send GOODBYE
  SendGoodbye(client_fd);
  // close connection.
  close(client_fd);
  return 0;
}

int Setup(char *dir) {
  printf("Crawling directory tree starting at: %s\n", dir);
  // Create a DocIdMap
  docs = CreateDocIdMap();
  CrawlFilesToMap(dir, docs);
  printf("Crawled %d files.\n", NumElemsInHashtable(docs));

  // Create the index
  docIndex = CreateMovieTitleIndex();

  if (NumDocsInMap(docs) < 1) {
    printf("No documents found.\n");
    return 0;
  }

  // Index the files
  printf("Parsing and indexing files...\n");
  ParseTheFiles(docs, docIndex);
  printf("%d entries in the index.\n", NumElemsInHashtable(docIndex->ht));
  return NumElemsInHashtable(docIndex->ht);
}

int Cleanup() {
  DestroyMovieTitleIndex(docIndex);
  DestroyDocIdMap(docs);
  return 0;
}

int main(int argc, char **argv) {
  char *port = NULL;
  char *dir_to_crawl = NULL;

  int debug_flag = 0;
  int c;

  while ((c = getopt(argc, argv, "dp:f:")) != -1) {
    switch (c) {
      case 'd':
        debug_flag = 1;
        break;
      case 'p':
        port = optarg;
        break;
      case 'f':
        dir_to_crawl = optarg;
        break;
      }
  }

  if (port == NULL) {
    printf("No port provided; please include with a -p flag.\n");
    exit(0);
  }

  if (dir_to_crawl == NULL) {
    printf("No directory provided; please include with a -f flag.\n");
    exit(0);
  }

  // Setup graceful exit
  struct sigaction kill;

  kill.sa_handler = sigint_handler;
  kill.sa_flags = 0;  // or SA_RESTART
  sigemptyset(&kill.sa_mask);

  if (sigaction(SIGINT, &kill, NULL) == -1) {
    perror("sigaction");
    exit(1);
  }

  int num_entries = Setup(dir_to_crawl);
  if (num_entries == 0) {
    printf("No entries in index. Quitting. \n");
    exit(0);
  }

  int s;
  // Step 1: Get address stuff

  memset(&hints, 0, sizeof(struct addrinfo));
  hints.ai_family = AF_INET;
  hints.ai_socktype = SOCK_STREAM;
  hints.ai_flags = AI_PASSIVE;

  s = getaddrinfo("localhost", port, &hints, &result);
  if (s != 0) {
    fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(s));
    exit(1);
  }
  // Step 2: Open socket
  int sock_fd = socket(AF_INET, SOCK_STREAM, 0);
  // Step 3: Bind socket
  if (bind(sock_fd, result->ai_addr, result->ai_addrlen) != 0) {
    perror("bind()");
    exit(1);
  }
  // Step 4: Listen on the socket
  if (listen(sock_fd, 10) != 0) {
    perror("listen()");
    exit(1);
  }
  struct sockaddr_in *result_addr = (struct sockaddr_in *) result->ai_addr;
  printf("Listening on file descriptor %d, port %d\n",
          sock_fd, ntohs(result_addr->sin_port));

  int yes = 1;
  while (1) {
    HandleClient(sock_fd);
    int reuse_socket =
      setsockopt(sock_fd, SOL_SOCKET, SO_REUSEADDR, &yes, sizeof(yes));
    if (reuse_socket == -1) {
      perror("setsockopt");
      exit(1);
    }
  }
  // Got Kill signal
  close(sock_fd);
  return 0;
}
