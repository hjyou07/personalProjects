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
#include <string.h>

#include "MovieSet.h"
#include "Movie.h"
#include "htll/LinkedList.h"


void DestroyMovieWrapper(void *a_movie) {
  DestroyMovie((Movie*)a_movie);
}

int AddMovieToSet(MovieSet set, Movie *movie) {
  int result = InsertLinkedList(set->movies, movie);
  return result;
}


MovieSet CreateMovieSet(char *desc) {
  MovieSet set = (MovieSet)malloc(sizeof(struct movieSet));
  if (set == NULL) {
    // Out of memory
    printf("Couldn't malloc for movieSet %s\n", desc);
    return NULL;
  }

  set->desc = (char*)malloc((strlen(desc) + 1) * sizeof(char));

  if (set->desc == NULL) {
    printf("Couldn't malloc for movieSet->desc");
    return NULL;
  }
  snprintf(set->desc, strlen(desc) + 1, "%s", desc);
  set->movies = CreateLinkedList();
  return set;
}

/**
 * Destroys the MovieSet.
 *
 * Frees the Movies in the set, and all char*s that are fields of the struct.
 */
void DestroyMovieSet(MovieSet set) {
  // TODO(Student): What else to do to clean up a MovieSet?
  // iterate thru the set->movies and clean up 
  // call destroylinkedlist onV set->movies, it needs a payload free function
  // what is our payload free function? payload = movie_struct
  // call DestroyMovie
  if (set != NULL) {
    if (set->movies != NULL) {
      DestroyLinkedList(set->movies, &DestroyMovieWrapper);
      set->movies = NULL;
    }
    if (set->desc != NULL) {
      free(set->desc);
      set->desc = NULL;
    }
    free(set);
    set = NULL;
  }
}