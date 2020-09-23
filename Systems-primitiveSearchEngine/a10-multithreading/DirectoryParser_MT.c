/*
 *  Created by Adrienne Slaughter
 *  CS 5007 Summer 2019
 *  Northeastern University, Seattle
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
#include <sys/time.h>
#include <time.h>
#include <pthread.h>

#include "DirectoryParser.h"
#include "MovieTitleIndex.h"
#include "Movie.h"
#include "DocIdMap.h"


#define BUFFER_SIZE 1000

//=======================
// To minimize the number of files we have, I'm
// putting the private function prototypes for
// the DirectoryParser here.

/**
 * Helper function to index a single file. 
 *
 * \return a pointer to the number of records (lines) indexed from the file
 */
void* IndexAFile_MT(void *toBeIter);

pthread_mutex_t ITER_MUTEX = PTHREAD_MUTEX_INITIALIZER; // global variable
pthread_mutex_t INDEX_MUTEX = PTHREAD_MUTEX_INITIALIZER; // global variable

// THINK: Why is this global?
// movieIndex will be the shared resource(global variable)
// and will be accessed by multiple threads
// (question) Do I bring HTIter out to global scope too?
MovieTitleIndex movieIndex;

int ParseTheFiles_MT(DocIdMap docs, MovieTitleIndex index, int num_threads) {
  clock_t start, end;
  double cpu_time_used;
  start = clock();

  movieIndex = index;
  // Create the iterator
  int num_records = 0;
  DocIdIter docs_iter = CreateDocIdIterator(docs);
  // Create the threads
  // Spawn the threads to work on the function IndexTheFile_MT
  pthread_t pthread[num_threads];
  for (int i = 0; i < num_threads; i++) {
    pthread_create(&pthread[i], NULL, IndexAFile_MT, (void *)docs_iter);
  }
  int *retval;
  for (int i = 0; i < num_threads; i++) {
    pthread_join(pthread[i], (void*)&retval);
    int thread_return = *retval;
    num_records += thread_return;
    //printf("num_records after thread[%d]: %d\n", i, num_records);
    free(retval);
  }
  DestroyHashtableIterator(docs_iter);
  pthread_mutex_destroy(&INDEX_MUTEX);
  pthread_mutex_destroy(&ITER_MUTEX);
  end = clock();
  cpu_time_used = ((double) (end - start)) / CLOCKS_PER_SEC;

  printf("Took %f seconds to execute. \n", cpu_time_used);

  return num_records;
}

// Returns the number of records indexed
int IndexTheFileHelper(char *file, uint64_t doc_id, MovieTitleIndex index) {
  FILE *cfPtr;

  if ((cfPtr = fopen(file, "r")) == NULL) {
    printf("File could not be opened\n");
    return 0;
  } else {
    // Read the file
    char buffer[BUFFER_SIZE];
    int row = 0;

    while (fgets(buffer, BUFFER_SIZE, cfPtr) != NULL) {
      // Create movie from row
      Movie *movie = CreateMovieFromRow(buffer);
      // Lock the index
      pthread_mutex_lock(&INDEX_MUTEX);
      // Add movie to index
      int result = AddMovieTitleToIndex(index, movie, doc_id, row);
      if (result < 0) {
        fprintf(stderr, "Didn't add MovieToIndex.\n");
      }
      row++;
      // Unlock the index
      pthread_mutex_unlock(&INDEX_MUTEX);
      DestroyMovie(movie);  // Done with this now
    }
    fclose(cfPtr);
    return row;
  }
}


void* IndexAFile_MT(void *docname_iter) {
  // Lock the iterator
  // Get the filename, unlock the iterator
  // Read the file
  // Create movie from row
  // Lock the index
  // Add movie to index
  // Unlock the index
  DocIdIter iter = (DocIdIter)docname_iter;
  HTKeyValue kvp;
  // Don't forget to free this at some point!!
  int* num_records = (int*)malloc(sizeof(int)); 
  *num_records = 0; 
  int counter = 0;
  //while (HTIteratorHasMore(iter) != 0) {
  int flag = 0;
  while (flag == 0) {
    // Lock the iterator
    pthread_mutex_lock(&ITER_MUTEX);
    if (HTIteratorGet(iter, &kvp) == 0) {
      // Get the filename, uinlock the iterator
      //counter++;
      //printf("num of files: %d\n", counter);
      char *file_name = kvp.value;
      uint64_t doc_id = kvp.key;
      pthread_mutex_unlock(&ITER_MUTEX);
    
      // Read the file and create movie from row using index lock
      *num_records += IndexTheFileHelper(file_name, doc_id, movieIndex);
    } else {
      pthread_mutex_unlock(&ITER_MUTEX);
    }
    pthread_mutex_lock(&ITER_MUTEX);
    if (HTIteratorHasMore(iter)) { 
      HTIteratorNext(iter);
    } else {
      //printf("Iterator doesn't have more\n");
      flag = 1;
      //printf("flag is now: %d\n", flag);
    }
    pthread_mutex_unlock(&ITER_MUTEX);
  }
  //printf("num of records: %d\n", *num_records);
  return (void*)num_records;
  
}
