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

#include "DocIdMap.h"
#include "Hashtable.h"

void DestroyString(void *val) {
    free(val);
}

DocIdMap CreateDocIdMap() {
  DocIdMap docs = (DocIdMap)CreateHashtable(64);
  return docs;
}

void DestroyDocIdMap(DocIdMap map) {
  DestroyHashtable(map, &DestroyString);
}

/**
 * Given a map and a pointer to a filename, puts the
 * filename in the map and gives it a unique ID.
 *
 * Assumes that the filename has been malloc'd
 * prior to being added to the map.
 *
 * Return 0 for success. 
 */
int PutFileInMap(char *filename, DocIdMap map) {
  // STEP 1: Put File in Map
  HTKeyValue kvp;
  HTKeyValue oldkvp;
  HTKeyValue* old_kvp = &oldkvp;
  // Ensure that each file/entry has a unique ID as a key
  int key = NumElemsInHashtable(map);
  key++;
  // Insert the id/filename into the Hashtable.
  kvp.key = key;
  kvp.value = filename;
  int putResult = PutInHashtable(map, kvp, old_kvp);
  if (putResult == 1) {
    printf("failure: e.g. no more memory!");
    return 1;
  }
  // If PutInHashtable returns 2 (there's a duplicate ID),
  // but this will never even be reached..
  if (putResult == 2) {
  // check if the filenames are the same. If not,
    if (strcmp(old_kvp->value, kvp.value) == 1) {
    // create a new ID for the file and insert it.
    // so hash it again with FNVHashInt64 with the output of FNVHash64
      int alt_key = NumElemsInHashtable(map);
      alt_key++;
      kvp.key = alt_key;
      PutInHashtable(map, kvp, old_kvp);
    } else {
      printf("already in the hashtable: wasn't put in the DocIdMap\n");
      return 1;
    }
  }
  return 0;
}

DocIdIter CreateDocIdIterator(DocIdMap map) {
  HTIter iter = CreateHashtableIterator(map);
  return iter;
}

void DestroyDocIdIterator(DocIdIter iter) {
  DestroyHashtableIterator(iter);
}

// Given a map and a docId, returns the relevant
// filename.
char *GetFileFromId(DocIdMap docs, int docId) {
  HTKeyValue kvp;
  // STEP 2: Return the pointer to the filename
  //  that corresponds to the given docid.
  int exists = LookupInHashtable(docs, (uint64_t)docId, &kvp);
  if (exists == 0) {
    return (char *)kvp.value;
  }
  // If there's an issue of some kind, return "FAIL".
  return "FAIL";
}
