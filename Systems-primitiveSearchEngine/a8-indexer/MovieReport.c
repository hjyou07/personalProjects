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

#include "MovieIndex.h"
#include "MovieReport.h"
#include "Movie.h"
#include "MovieSet.h"
#include "htll/LinkedList.h"
#include "htll/Hashtable.h"


void PrintReport(Index index) {
  // Create Iter
  HTIter iter = CreateHashtableIterator(index);

  HTKeyValue movie_set;

  HTIteratorGet(iter, &movie_set);
  // why does it say Use of uninitialised value of size 8?
  // it's probably not finding this pointer..
  printf("indexType: %s\n", (char*)((MovieSet)movie_set.value)->desc);
  printf("%d items\n", NumElementsInLinkedList((LinkedList)((MovieSet)(movie_set.value))->movies));
  OutputMovieSet((MovieSet)movie_set.value);
  while (HTIteratorHasMore(iter)) {
    HTIteratorNext(iter);
    HTIteratorGet(iter, &movie_set);
  printf("indexType: %s\n", (char*)((MovieSet)movie_set.value)->desc);
  printf("%d items\n", NumElementsInLinkedList((LinkedList)((MovieSet)(movie_set.value))->movies));
    OutputMovieSet((MovieSet)movie_set.value);
  }
  // For every movie set, create a LLIter
  DestroyHashtableIterator(iter);
}

void OutputMovieSet(MovieSet movie_set) {
  // TODO(Student): Print the MovieSet to the terminal.
  // movie_set is a linked list. I want movies printed out.
  // I need to access (Movie*)movie_set->movies->payload. Use LLIter.
  LinkedList movies = movie_set->movies;
  // so this is a fencepost problem, and I don't know how to resolve it
  // unless I take one "post" out and print it outside the while loop.
  // but there's gotta be a way I can refactor this..im stoopid
  LLIter movies_iter = CreateLLIter(movies);
  Movie* curr_movie;
  LLIterGetPayload(movies_iter, (void**)&curr_movie);
  char* title = curr_movie->title;
  printf("  %s\n", title);
  while (LLIterHasNext(movies_iter)) {
    LLIterNext(movies_iter);
    LLIterGetPayload(movies_iter, (void**)&curr_movie);
    title = curr_movie->title;
    printf("  %s\n", title);
  }
  DestroyLLIter(movies_iter);
}

