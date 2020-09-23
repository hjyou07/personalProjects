// CS 5007, Northeastern University, Seattle
// Summer 2019
// Adrienne Slaughter
//
// Inspired by UW CSE 333; used with permission.
//
// This is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published
//  by the Free Software Foundation, either version 3 of the License,
//  or (at your option) any later version.
// It is distributed in the hope that it will be useful, but
//  WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//  General Public License for more details.

#include "LinkedList.h"
#include "LinkedList_priv.h"
#include "Assert007.h"

#include <stdio.h>
#include <stdlib.h>

LinkedList CreateLinkedList() {
  LinkedList list = (LinkedList)malloc(sizeof(LinkedListHead));
  if (list == NULL) {
    // out of memory
    return (LinkedList) NULL;
  }
  // Step 1.
  // initialize the newly allocated record structure
  list->num_elements = 0;
  list->head = NULL;
  list->tail = NULL;
  return list;
}

int DestroyLinkedList(LinkedList list,
                      LLPayloadFreeFnPtr payload_free_function) {
  Assert007(list != NULL);
  Assert007(payload_free_function != NULL);

  // Step 2.
  // Free the payloads, as well as the nodes
    while (list->head != NULL) {
    // copy the head pointer into a temp variable
    LinkedListNodePtr curr_node = list->head;
    // free the data in the head, accessing via alias variable
    payload_free_function(curr_node->payload);
    // have the temp pointer point to the next node
    curr_node = list->head->next;
    // because we want to destroy the current head node
    DestroyLinkedListNode(list->head);
    // now I can have the head pointer point to the temp pointer,
    // which had saved the next node.
    list->head = curr_node;
  }
    free(list);
    return 0;
}

unsigned int NumElementsInLinkedList(LinkedList list) {
  Assert007(list != NULL);
  return list->num_elements;
}

LinkedListNodePtr CreateLinkedListNode(void *data) {
    LinkedListNodePtr node = (LinkedListNodePtr)malloc(sizeof(LinkedListNode));
    if (node == NULL) {
        // Out of memory
        return NULL;
    }
    node->payload = data;
    node->next = NULL;
    node->prev = NULL;

    return node;
}

int DestroyLinkedListNode(LinkedListNode *node) {
  Assert007(node != NULL);
  node->payload = NULL;
  node->next = NULL;
  node->prev = NULL;
  free(node);
  return 0;
}

int InsertLinkedList(LinkedList list, void *data) {
  Assert007(list != NULL);
  Assert007(data != NULL);
  LinkedListNodePtr new_node = CreateLinkedListNode(data);

  if (new_node == NULL) {
    return 1;
  }

  if (list->num_elements == 0) {
    Assert007(list->head == NULL);  // debugging aid
    Assert007(list->tail == NULL);  // debugging aid
    list->head = new_node;
    list->tail = new_node;
    new_node->next = new_node->prev = NULL;
    list->num_elements = 1U;
    return 0;
  }

  // Step 3.
  // typical case; list has >=1 elements
  Assert007(list->head != NULL);
  // adding to the head of the list, set the current head
  // as the next node in new_node
  new_node->next = list->head;
  // and make the current head's prev node new_node
  list->head->prev = new_node;
  // now the new_node is the head, there's noting previou to new_node
  new_node->prev = NULL;
  // NOW set the list's head to the new node (after all the adjustments)
  list->head = new_node;
  list->num_elements++;
  return 0;
}

int AppendLinkedList(LinkedList list, void *data) {
  // Step 5: implement AppendLinkedList.  It's kind of like
  // InsertLinkedList, but add to the end instead of the beginning.
  Assert007(list != NULL);
  Assert007(data != NULL);
  LinkedListNodePtr new_node = CreateLinkedListNode(data);

  if (new_node == NULL) {
    return 1;
  }

  if (list->num_elements == 0) {
    Assert007(list->head == NULL);  // debugging aid
    Assert007(list->tail == NULL);  // debugging aid
    list->head = new_node;
    list->tail = new_node;
    new_node->next = new_node->prev = NULL;
    list->num_elements = 1U;
    return 0;
  }
  // so this is just basically InserttLinkedList but next swapped with prev,
  // head swapped with tail.
  Assert007(list->tail != NULL);
  new_node->prev = list->tail;
  list->tail->next = new_node;
  new_node->next = NULL;
  list->tail = new_node;
  list->num_elements++;
  return 0;
}

int PopLinkedList(LinkedList list, void **data) {
  // data = pointer to the pointer to data alias pointer of the address to data)
  // which means data = pointer to the payload (payload=pointer to the data)
  Assert007(list != NULL);
  Assert007(data != NULL);

  // Step 4: implement PopLinkedList.  Make sure you test for
  // and empty list and fail.  If the list is non-empty, there
  // are two cases to consider: (a) a list with a single element in it
  // and (b) the general case of a list with >=2 elements in it.
  // Be sure to call free() to deallocate the memory that was
  // previously allocated by InsertLinkedList().
  if (list->num_elements == 0) {
    return 1;
  }
  // list->head->payload is a [pointer to the data]
  // variable data is a pointer to the above pointer
  // data -> ([pointer to the data] -> data )
  // so I need to set the VALUE of the data variable
  // to [the pointer to the data], so DEREFERENCE it
  *data = list->head->payload;
  // create a temp variable to save the head
  LinkedListNodePtr curr_node = list->head;
  // take care of two cases stated above
  if (list->num_elements == 1) {
    list->head = list->tail = NULL;
  } else {
    // use the temp variable to change the head,
    list->head = curr_node->next;
    // because I still need access to "current" head
    // or i can just do this operation first then directly change the head
    list->head->prev = NULL;
  }
  // the following operations are for both cases, so factored out
  list->num_elements--;
  DestroyLinkedListNode(curr_node);
  return 0;
}

int SliceLinkedList(LinkedList list, void **data) {
  // Step 6: implement SliceLinkedList.
  Assert007(list != NULL);
  Assert007(data != NULL);

  if (list->num_elements == 0) {
    return 1;
  }
  // like in PopLinkedList,
  // we have a variable data which is a pointer to the pointer to the data.
  // alias, the pointer to the address to the data.
  // list->tail->payload is the pointer(address) to the data.
  // I want the variable data to POINT at that address,
  // so I DEREFERENCE data (*data) and write the address in there.
  *data = list->tail->payload;
  LinkedListNodePtr curr_node = list->tail;
  // basically same thing as PopLinkedList, but head<->tail, next<->prev.
  if (list->num_elements == 1) {
    list->head = list->tail = NULL;
  } else {
    list->tail = curr_node->prev;
    list->tail->next = NULL;
  }
  list->num_elements -= 1;
  DestroyLinkedListNode(curr_node);
  return 0;
}

void SortLinkedList(LinkedList list,
                    unsigned int ascending,
                    LLPayloadComparatorFnPtr compare) {
    Assert007(list != NULL);
    if (list->num_elements <2) {
        return;
    }

    int swapped;
    do {
      LinkedListNodePtr curnode = list->head;
      swapped = 0;

      while (curnode->next != NULL) {
        // compare this node with the next; swap if needed
        int compare_result = compare(curnode->payload, curnode->next->payload);

        if (ascending) {
          compare_result *= -1;
        }

        if (compare_result < 0) {
          // swap
          void* tmp;
          tmp = curnode->payload;
          curnode->payload = curnode->next->payload;
          curnode->next->payload = tmp;
          swapped = 1;
        }
        curnode = curnode->next;
      }
    } while (swapped);
}

void PrintLinkedList(LinkedList list) {
    printf("List has %lu elements. \n", list->num_elements);
}


LLIter CreateLLIter(LinkedList list) {
  Assert007(list != NULL);
  Assert007(list->num_elements > 0);

  LLIter iter = (LLIter)malloc(sizeof(struct ll_iter));
  Assert007(iter != NULL);

  iter->list = list;
  iter->cur_node = list->head;
  return iter;
}

int LLIterHasNext(LLIter iter) {
  Assert007(iter != NULL);
  return (iter->cur_node->next != NULL);
}

int LLIterNext(LLIter iter) {
  Assert007(iter != NULL);
  // Step 7: if there is another node beyond the iterator, advance to it,
  // and return 0. If there isn't another node, return 1.
  if (LLIterHasNext(iter)) {
    iter->cur_node = iter->cur_node->next;
    return 0;
  }
  return 1;
}

int LLIterGetPayload(LLIter iter, void** data) {
  Assert007(iter != NULL);
  *data = iter->cur_node->payload;
  return 0;
}


int LLIterHasPrev(LLIter iter) {
  Assert007(iter != NULL);
  return (iter->cur_node->prev != NULL);
}

int LLIterPrev(LLIter iter) {
  Assert007(iter != NULL);
  // Step 8:  if there is another node beyond the iterator, go to it,
  // and return 0. If not return 1.
  if (LLIterHasPrev(iter)) {
    iter->cur_node = iter->cur_node->prev;
    return 0;
  }
  return 1;
}

int DestroyLLIter(LLIter iter) {
  Assert007(iter != NULL);
  iter->cur_node = NULL;
  iter->list = NULL;
  free(iter);
  return 0;
}

int LLIterInsertBefore(LLIter iter, void* payload) {
  Assert007(iter != NULL);
  if ((iter->list->num_elements <= 1) ||
      (iter->cur_node == iter->list->head)) {
    // insert item
    return InsertLinkedList(iter->list, payload);
  }

  LinkedListNodePtr new_node = CreateLinkedListNode(payload);
  if (new_node == NULL) return 1;

  new_node->next = iter->cur_node;
  new_node->prev = iter->cur_node->prev;
  iter->cur_node->prev->next = new_node;
  iter->cur_node->prev = new_node;

  iter->list->num_elements++;

  return 0;
}

int LLIterDelete(LLIter iter, LLPayloadFreeFnPtr payload_free_function) {
  Assert007(iter != NULL);

  // Step 9: implement LLIterDelete. There are several cases
  // to consider:
  //
  // - Case 1: the list becomes empty after deleting.
  // - Case 2: iter points at head
  // - Case 3: iter points at tail
  // - fully general case: iter points in the middle of a list,
  //                       and you have to "splice".
  //
  // Be sure to call the payload_free_function to free the payload
  // the iterator is pointing to, and also free any LinkedList
  // data structure element as appropriate.
  payload_free_function(iter->cur_node->payload);
  iter->list->num_elements--;
  // if this is the only element
  if (!LLIterHasPrev(iter) && !LLIterHasNext(iter)) {
    DestroyLinkedListNode(iter->cur_node);
    DestroyLLIter(iter);
    iter->list->head = NULL;
    iter->list->tail = NULL;
    return 1;
  }
  // if this is the first node in the iterator
  if (!LLIterHasPrev(iter)) {
    // set the head to point at the next node
    iter->list->head = iter->list->head->next;
    // set the updated head node to point at null
    iter->list->head->prev = NULL;
    // destory the current node the iterator's pointing at
    // I didn't have to create a temp variable to store the list->head,
    // because I have access to it via iter->cur_head
    DestroyLinkedListNode(iter->cur_node);
    // now i set the iterator's curren't node to point at the head I changed.
    iter->cur_node = iter->list->head;
  // kind of the same thing as above, but removing the last node
  // then walk backwards
  } else if (!LLIterHasNext(iter)) {
    iter->list->tail = iter->list->tail->prev;
    iter->list->tail->next = NULL;
    DestroyLinkedListNode(iter->cur_node);
    // kind of the same thing as above, but removing the last node
    // then walk backwards
    iter->cur_node = iter->list->tail;
  // when the iterator to remove is in the middle somewhere,
  // I need to "splice"
  } else {
    LinkedListNodePtr next_node = iter->cur_node->next;
    iter->cur_node->prev->next = iter->cur_node->next;
    iter->cur_node->next->prev = iter->cur_node->prev;
    DestroyLinkedListNode(iter->cur_node);
    iter->cur_node = next_node;
  }
  return 0;
}
