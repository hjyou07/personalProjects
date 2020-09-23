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
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <assert.h>

#include "MovieIndex.h"
#include "htll/LinkedList.h"
#include "htll/Hashtable.h"
#include "Movie.h"
#include "MovieSet.h"

#define DEFAULT_INDEX_SIZE 100

void DestroyMovieSetWrapper(void *movie_set) {
  DestroyMovieSet((MovieSet)movie_set);
}

void toLower(char *str, int len) {
  for (int i = 0; i < len; i++) {
    str[i] = tolower(str[i]);
  }
}

Index BuildMovieIndex(LinkedList movies, enum IndexField field_to_index) {
  // TODO(Student): This 100 is a magic number. 
  // Is there a better way to initialize this? If so, do it. 
  Index movie_index = CreateHashtable(DEFAULT_INDEX_SIZE);

  // TODO: Check that there is at least one movie
  // What happens if there is not at least one movie?
  if (NumElementsInLinkedList(movies) < 1) {
    printf("There are no movies to be indexed ");
  } else {
    LLIter iter = CreateLLIter(movies);
    Movie* cur_movie;
    LLIterGetPayload(iter, (void**)&cur_movie);
  
    int result = AddMovieToIndex(movie_index, cur_movie, field_to_index);
    assert(result == 0);  
    while (LLIterHasNext(iter)) {
      LLIterNext(iter);
      LLIterGetPayload(iter, (void**)&cur_movie);
      result = AddMovieToIndex(movie_index, cur_movie, field_to_index);
    }
    DestroyLLIter(iter);
  }
  return movie_index;
}


int AddMovieActorsToIndex(Index index, Movie *movie) {
  HTKeyValue kvp;
  HTKeyValue* kvp_pointer = &kvp;
  HTKeyValue oldkvp;
  HTKeyValue* old_kvp = &oldkvp;
  MovieSet movie_set;
  // TODO(Student): Add movies to the index via actors. 
  // so I have to create a movie_set with char* desc of EACH actors,
  // I need a walk through the char **actor_list first THEN generate key for each
  for (int i = 0; i < movie->num_actors; i++) {
    // now for a particular actor, generate a key
    uint64_t key = FNVHash64((unsigned char*)movie->actor_list[i],
                         strlen(movie->actor_list[i]));
    int set_exists = LookupInHashtable(index, key, kvp_pointer);
    if (set_exists == 0) {
      movie_set = (MovieSet)kvp.value;
    } else {
      movie_set = CreateMovieSet(movie->actor_list[i]);
      kvp.key = key;
      kvp.value = movie_set;
      int success = PutInHashtable(index, kvp, old_kvp);
      assert(success == 0);
    }
    AddMovieToSet(movie_set, movie);
    //printf("num_buckets: %d\n", index->num_buckets);
    //printf("num_elements: %d\n", index->num_elements);
    //printf("MovieSet desc: %s\n", movie_set->desc);
  }
  return 0;
}

int AddMovieToIndex(Index index, Movie *movie, enum IndexField field) {
  // kvp is a key value pair struct, and I can access its fields by . not ->,
  // because it's the struct itself not a pointer to the struct
  HTKeyValue kvp;
  HTKeyValue* kvp_pointer = &kvp;
  HTKeyValue oldkvp;
  HTKeyValue* old_kvp = &oldkvp;
  MovieSet movie_set;
  uint64_t key = ComputeKey(movie, field);
  // I think I need switch cases cos the set description is dependent on field?
  switch (field) {
    case Actor:
      return AddMovieActorsToIndex(index, movie);
    case Genre: ;
      // TODO(Student): How do we add movies to the index? 
      // Check hashtable to see if relevant MovieSet already exists
      int genre_set_exists = LookupInHashtable(index, key, kvp_pointer);
      // If it does, grab access to MovieSet from the hashtable
      if (genre_set_exists == 0) {
        movie_set = (MovieSet)kvp.value;
      } else {
      // If it doesn't, create the new MovieSet with description and get the pointer to it
        movie_set = CreateMovieSet(movie->genre);
        kvp.key = key;
        kvp.value = movie_set;
        int genre_success = PutInHashtable(index, kvp, old_kvp);
        // below ensures that the key wans't already in the hashtable
        assert(genre_success == 0);
      } 
      // After we either created or retrieved the MovieSet from the Hashtable: 
      AddMovieToSet(movie_set, movie);
      break;
    case StarRating: ;
      int star_set_exists = LookupInHashtable(index, key, kvp_pointer);
      if (star_set_exists == 0) {
        movie_set = (MovieSet)kvp.value;
      } else {
        // star rating isn't a string, it's a double. so I need to convert it to string
        // for me to be able to create a MovieSet with a char* desc argument
        char rating_str[10];
        snprintf(rating_str, 10, "%f", movie->star_rating);
        movie_set = CreateMovieSet(rating_str);
        kvp.key = key;
        kvp.value = movie_set;
        int content_r_success = PutInHashtable(index, kvp, old_kvp);
        assert(content_r_success == 0);
      }
      AddMovieToSet(movie_set, movie);
      break;
    case ContentRating: ;
      int content_rate_set_exists = LookupInHashtable(index, key, kvp_pointer);
      if (content_rate_set_exists == 0) {
        movie_set = (MovieSet)kvp.value;
      } else {
        movie_set = CreateMovieSet(movie->content_rating);
        kvp.key = key;
        kvp.value = movie_set;
        int content_r_success = PutInHashtable(index, kvp, old_kvp);
        assert(content_r_success == 0);
      } 
      AddMovieToSet(movie_set, movie);
      break;
  }
 // printf("num_buckets: %d\n", index->num_buckets);
 // printf("num_elements: %d\t", index->num_elements);
 // printf("MovieSet desc: %s\n", movie_set->desc);

  return 0;
}

uint64_t ComputeKey(Movie* movie, enum IndexField which_field) {
  char rating_str[10];
  switch (which_field) {
    case Genre:
      return FNVHash64((unsigned char*)movie->genre, strlen(movie->genre));
    case StarRating:
      snprintf(rating_str, 10, "%f", movie->star_rating);
      return FNVHash64((unsigned char*)rating_str, strlen(rating_str));
    case ContentRating:
      return FNVHash64((unsigned char*)movie->content_rating,
                       strlen(movie->content_rating));
    case Actor:
      break;
  }
  return -1u;
}

MovieSet GetMovieSet(Index index, const char *term) {
  HTKeyValue kvp;
  char lower[strlen(term)+1];
  snprintf(lower, strlen(term) + 1, "%s", term);
  toLower(lower, strlen(lower));
  int result = LookupInHashtable(index, FNVHash64((unsigned char*)lower,
                                                  (unsigned int)strlen(lower)), &kvp);
  if (result < 0) {
    printf("term couldn't be found: %s \n", term);
    return NULL;
  }
  return (MovieSet)kvp.value;
}

int DestroyIndex(Index index) {
  DestroyHashtable(index, &DestroyMovieSetWrapper);
  return 0;
}

Index CreateIndex() {
  return CreateHashtable(128);
}
