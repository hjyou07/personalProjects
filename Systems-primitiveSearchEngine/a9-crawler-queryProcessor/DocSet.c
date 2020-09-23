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
#include <string.h>
#include <assert.h>

#include "DocSet.h"
#include "Hashtable.h"
#include "Util.h"

/**
 * Adds a reference to a doc/row to the set.
 *
 * \param set The DocumentSet to add the movie to
 * \param doc_id Which document/file the movie is stored in
 * \param row_id Which row in the file the movie can be found.
 *
 * \return 0 if successful.
 */
int AddDocInfoToSet(DocumentSet set, uint64_t docId, int rowId) {
  // STEP 4: Implement AddDocInfoToSet.
  HTKeyValue kvp;
  HTKeyValue oldkvp;
  /** IMPORTANT: your invalid free error comes from here!
  * You need to malloc the rowId since it goes in as a payload in LinkedList!
  * tyty andy for giving some insights ohmygod what a moment
  */
  int *rowIdHeap = (int *)malloc(sizeof(int));
  *rowIdHeap = rowId;
  // The hashtable(doc_index) is a member of the struct docSet,
  // hence access it by set->doc_index.
  // but I need to add a LinkedList of rowIds into the hashtable doc_index
  int search_result = LookupInHashtable(set->doc_index, docId, &oldkvp);
  if (search_result == 0) {
  // if the search was successful
    // put rowID in the value of the oldkvp, this is inserting to a LinkedList
    // I need to check if this rowId is already in the LinkedList of kvp.value
    // umm..ok going to ignore it for now but maybe refer to a8/BuildMovieIndex?
    int add_result = InsertLinkedList(oldkvp.value, rowIdHeap);
    assert(add_result == 0);
    return add_result;
  } else {
  // if the docId isn't in the hashtable(doc_index)
    // the uint64_t docId (KEY) and LinkedList of rowIds (VALUE)
    // needs to be paired via kvp
    kvp.key = docId;
    LinkedList rowIdList = CreateLinkedList();
    kvp.value = rowIdList;
    // now I'm goint go add the given rowId into the kvp.value = LinkedList
    int add_result = InsertLinkedList(kvp.value, rowIdHeap);
    assert(add_result == 0);
    // now I have created a new key value pair for docId
    // and EMPTY rowId LinkedList,
    // then putting in in the hash table(doc_index)
    int put_result = PutInHashtable(set->doc_index, kvp, &oldkvp);
    assert(put_result == 0);
    return put_result;
  }
  // Make sure there are no duplicate rows or docIds.
  return -1;
}

/**
 * Determines if a DocumentSet contains movies from a specifid
 * document or file.
 *
 * \param set The DocumentSet to query
 * \param doc_id Which doc to look for.
 *
 * \return 0 if the doc_id is found, -1 otherwise.
 */
int DocumentSetContainsDoc(DocumentSet set, uint64_t docId) {
  HTKeyValue kvp;
  // STEP 5: Implement DocumentSetContainsDoc
  return LookupInHashtable(set->doc_index, docId, &kvp);
}

void PrintOffsetList(LinkedList list) {
  printf("Printing offset list\n");
  LLIter iter = CreateLLIter(list);
  int* payload;
  while (LLIterHasNext(iter) != 0) {
    LLIterGetPayload(iter, (void**)&payload);
    printf("%d\t", *((int*)payload));
    LLIterNext(iter);
  }
  LLIterGetPayload(iter, (void**)&payload);
  printf("%d\n", *((int*)payload));
  DestroyLLIter(iter);
}


DocumentSet CreateDocumentSet(char *desc) {
  DocumentSet set = (DocumentSet)malloc(sizeof(struct docSet));
  if (set == NULL) {
    // Out of memory
    printf("Couldn't malloc for movieSet %s\n", desc);
    return NULL;
  }
  int len = strlen(desc);
  set->desc = (char*)malloc((len + 1) *  sizeof(char));
  if (set->desc == NULL) {
    printf("Couldn't malloc for movieSet->desc");
    return NULL;
  }
  snprintf(set->desc, len + 1, "%s", desc);
  set->doc_index = CreateHashtable(16);
  return set;
}


void DestroyOffsetList(void *val) {
  LinkedList list = (LinkedList)val;
  DestroyLinkedList(list, &SimpleFree);
}

void DestroyDocumentSet(DocumentSet set) {
  // Free desc
  free(set->desc);
  // Free doc_index
  DestroyHashtable(set->doc_index, &DestroyOffsetList);
  // Free set
  free(set);
}
