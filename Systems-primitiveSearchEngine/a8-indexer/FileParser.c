/*
 *  Adrienne Slaughter
 *  5007 Spr 2020
 *
 *  This is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  It is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  See <http://www.gnu.org/licenses/>.
 */

#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>
#include <ctype.h>
#include <string.h>

#include "FileParser.h"
#include "Movie.h"
#include "MovieIndex.h"

#define NUM_FIELDS 6
#define MAX_ROW_LENGTH 1000
#define MAX_NUM_ACTORS 10

char* CheckAndAllocateString(char* token) {
  if (strcmp("-", token) == 0) {
    return NULL;
  } else {
    char *out = (char *) malloc((strlen(token) + 1) * sizeof(char));
    // TODO(adrienne): remove when confirmed    strcpy(out, token);
    snprintf(out, strlen(token) + 1, "%s", token);
    return out;
  }
}

int CheckInt(char* token) {
  if (strcmp("-", token) == 0) {
    return -1;
  } else {
    return atoi(token);
  }
}

double CheckDouble(char* token) {
  if (strcmp("-", token) == 0) {
    return -1;
  } else {
    return atof(token);
  }
}

Movie* CreateMovieFromRow(char *data_row) {
  Movie* mov = CreateMovie();
  if (mov == NULL) {
    printf("Couldn't create a Movie.\n");
    return NULL;
  }
  // TODO(Student): Parse the row to create and populate a Movie.
  const char* delimiter = "|";
  const char* actor_delimiter = ",";
  char* token;
  int count = 0;
  // count the number of tokens, I'm going to loop over this
  for (token = strtok_r(data_row, delimiter, &data_row) ; token != NULL;
        token = strtok_r(NULL, delimiter, &data_row)) {
      count++;
      if (count == 1) {
        double star = CheckDouble(token);
        mov->star_rating = star;
      }
      if (count == 2) {
        char* title = CheckAndAllocateString(token);
        mov->title = title;
      }
      if (count == 3) {
        char* rated = CheckAndAllocateString(token);
        mov->content_rating = rated;
      }
      if (count == 4) {
        char* genre = CheckAndAllocateString(token);
        mov->genre = genre;
      }
      if (count == 5) {
        int runtime = CheckInt(token);
        mov->duration = runtime;
      }
      if (count == 6) {
        // this saves the whole line of actor list as a whole string
        // I need to free this later in this loop
        char* actors = token;
        // I'm going to save the actor tokens in an temp array on STACK
        char* actor_list_on_stack[MAX_NUM_ACTORS];
        int i = 0;
        for (char* actor_token = strtok_r(actors, actor_delimiter, &actors) ;
              actor_token != NULL;
              actor_token = strtok_r(NULL, actor_delimiter, &actors)) {
          char* actor = CheckAndAllocateString(actor_token);
          // populate the temp array on STACK with the malloced actor pointers
          actor_list_on_stack[i] = actor;
          i++;
        }
        // i was the counter starting from zero, and number of actors would be i + 1
        int num_actors = i++;
        // set mov->num_actors
        mov->num_actors = num_actors;
        // now I can malloc memory to store array of char pointers of size num_actors
        char** actor_list = (char **) malloc(num_actors * sizeof(char*));
        for (i = 0; i < num_actors; i++) {
          // now populate the MALLOCED actor_list with the pointers to MALLOCED actors
          // stored in temporary actor_list ON STACK
          actor_list[i] = actor_list_on_stack[i];
          // set mov->actor_list
          mov->actor_list = actor_list;
        }
      }
  }
  return mov;
}

// Returns a LinkedList of Movie structs from the specified file
LinkedList ReadFile(char* filename) {
  FILE *cfPtr;

  LinkedList movie_list = CreateLinkedList();

  if ((cfPtr = fopen(filename, "r")) == NULL) {
    printf("File could not be opened\n");
    DestroyLinkedList(movie_list, NULL);
    return NULL;
  } else {
    char* row = NULL;
    ssize_t read;
    size_t len = 0;

    while ((read = getline(&row, &len, cfPtr)) != -1) {
      // Got the line; create a movie from it
      MoviePtr movie = CreateMovieFromRow(row);
      if (movie != NULL) {
        InsertLinkedList(movie_list, movie);
      }
    }
    free(row);
    fclose(cfPtr);
  }
  return movie_list;
}
