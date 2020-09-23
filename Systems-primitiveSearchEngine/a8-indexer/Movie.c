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
#include <assert.h>

#include "Movie.h"

/**
 *  Creates a movie.
 *
 *  Returns a pointer to an unpopulated Movie (initialized with default/null values).
 */
Movie* CreateMovie() {
  Movie *mov = (Movie*)malloc(sizeof(Movie));
  if (mov == NULL) {
    printf("Couldn't allocate more memory to create a Movie\n");
    return NULL;
  }
  // so I do need to fucking initialize on my own.
  mov->title = NULL;
  mov->star_rating = -1;
  mov->content_rating = NULL;
  mov->genre = NULL;
  mov->duration = -1;
  mov->actor_list = NULL;
  mov->num_actors = -1;

  assert(mov->title == NULL);
  assert(mov->star_rating == -1);
  return mov;
}

/**
 *  Destroys a movie, freeing up all char*s in the struct.
 */
void DestroyMovie(Movie* movie) {
  // TODO(Student): Make sure the movie is destroyed properly. 
  if (movie != NULL) {
    if (movie->title != NULL) {
      free(movie->title);
      movie->title = NULL;
    }
    if (movie->content_rating != NULL) {
      free(movie->content_rating);
      movie->content_rating = NULL;
    }
    if (movie->genre != NULL) {
      free(movie->genre);
      movie->genre = NULL;
    }
    // I malloced actor_list AND actors, so need to free them all
    if (movie->actor_list != NULL) {
      for (int i = 0; i < movie->num_actors; i++) {
        free(movie->actor_list[i]);
        movie->actor_list[i] = NULL;
      }
      free(movie->actor_list);
      movie->actor_list = NULL;
    }
    free(movie);
    movie = NULL;
  }
}



