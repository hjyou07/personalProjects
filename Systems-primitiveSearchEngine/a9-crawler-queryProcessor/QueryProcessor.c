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
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <assert.h>

#include "QueryProcessor.h"
#include "MovieTitleIndex.h"
#include "LinkedList.h"
#include "Hashtable.h"
#include "DocSet.h"

/** 1.User Enters a query term 
*   2.QueryProcessor uses MovieTitleIndex to find the entry that has a corresponding key.
*     This returns a Hashtable doc_index [DocId, LinkedList[RowIds]]
*   3.Iterate through the returned hashtable(doc_index) for each document.
*     3.1.Use DocIdMap to find the filename of the document
*     3.2.Open the file, find the correct row
*     3.3.Create a Movie struct from the roRw
*     3.4.Put it into a MovieIndex (the one we built from A8)
*     3.5.Repeat for all RowIds in the LinkedList[RowIds]
*   4.After all Movies have been loaded, use the MovieIndex to PrintReport nicely.
*/

SearchResultIter CreateSearchResultIter(DocumentSet set) {
  // when is it impossible for me to create an iterator?
  if (NumElemsInHashtable(set->doc_index) == 0) {
    printf("There's nothing to iterate over");
    return NULL;
  }
  SearchResultIter iter =
    (SearchResultIter)malloc(sizeof(struct searchResultIter));
    // STEP 7: Implement the initialization of the iter.
  if (iter == NULL) {
    printf("Can't allocate memory for SearchResultIter");
    return NULL;
  }
  // initialize fields: int cur_doc_id; HTIter doc_iter; LLIter offset_iter;
  HTKeyValue kvp; 
  // Linkedlist kvp.value -> how do I connect doc_index to kvp?
  iter->doc_iter = CreateHashtableIterator(set->doc_index);
  // this would save the current payload of the iterator in kvp
  HTIteratorGet(iter->doc_iter, &kvp);
  // now I connected the struct kvp to hashtable doc_index
  // just to avoid invalid pointer segfault
  iter->cur_doc_id = kvp.key;
  iter->offset_iter = CreateLLIter(kvp.value);
  // what do i pass on as an argument to CreateLLIter in CreateSearchResultIter?
  return iter;
}

void DestroySearchResultIter(SearchResultIter iter) {
  // Destroy LLIter
  if (iter->offset_iter != NULL) {
    DestroyLLIter(iter->offset_iter);
  }
  // Destroy doc_iter
  DestroyHashtableIterator(iter->doc_iter);
  free(iter);
}

SearchResultIter FindMovies(MovieTitleIndex index, char *term) {
  DocumentSet set = GetDocumentSet(index, term);
  if (set == NULL) {
    return NULL;
  }
  printf("Getting docs for movieset term: \"%s\"\n", set->desc);
  SearchResultIter iter = CreateSearchResultIter(set);
  return iter;
}

/**
* A SearchResult is a pointer to the struct searchResult with members:
* doc_id and row_id, representing a particular row in a particular file. 
*/
int SearchResultGet(SearchResultIter iter, SearchResult output) {
  // STEP 9: Implement SearchResultGet
  int *rowId;
  output->doc_id = (iter->cur_doc_id);
  // void **: pointer to the address of the data
  LLIterGetPayload(iter->offset_iter, (void *)&rowId);
  // rowId is a pointer to an int. Dereference to get an int.
  output->row_id = *rowId;
  return 0;
}

int SearchResultNext(SearchResultIter iter) {
  // STEP 8: Implement SearchResultNext
  if (SearchResultIterHasMore(iter)) {
    // two cases: where there's more left in a specific LinkedList kvp.value
    if (LLIterHasNext(iter->offset_iter)) {
      LLIterNext(iter->offset_iter);
    } else {
    // where there's more left in Hashtable doc_index
      HTIteratorNext(iter->doc_iter);
      DestroyLLIter(iter->offset_iter);
      HTKeyValue kvp;
      HTIteratorGet(iter->doc_iter, &kvp);
      // now I connected the struct kvp to hashtable doc_index
      iter->cur_doc_id = kvp.key;
      iter->offset_iter = CreateLLIter(kvp.value);
    }
    // return 0 if it was successfully stepped over to the next element
    return 0;
  }
  return 1;
}

// Return 0 if no more
int SearchResultIterHasMore(SearchResultIter iter) {
  if (iter->doc_iter == NULL) {
  // make sure that the iterator isn't null, can be replaced with assert()
    return 0;
  }
  if (LLIterHasNext(iter->offset_iter) == 0) {
  // if the rowid has no more to iterate to
    return (HTIteratorHasMore(iter->doc_iter));
    // return 0: docid has no more to iterate to
    // return 1: docid has some more to iterate to
  }
  return 1;
}
