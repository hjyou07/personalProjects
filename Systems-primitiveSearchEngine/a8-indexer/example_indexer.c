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
#include <assert.h>
#include <string.h>

#include <ctype.h>
#include <unistd.h>


#include "htll/LinkedList.h"
#include "MovieIndex.h"
#include "Movie.h"
#include "MovieReport.h"
#include "./FileParser.h"

void DestroyNothing(void* thing) {
  // Helper method to destroy the LinkedList.
  DestroyMovie((Movie*)thing);
}

int main(int argc, char* argv[]) {
  // TODO(Student): Check args, do the right thing.
  // sample invocation: ./example -g data/test
  // there will be three total arguments 
  // argv[0] = ./example, argv[1] = -g, argv[2] = data/test
  enum IndexField field;
  char* flag = argv[1];
  if (strcmp(flag, "-s") == 0) {field = StarRating;}
  if (strcmp(flag, "-c") == 0) {field = ContentRating;}
  if (strcmp(flag, "-g") == 0) {field = Genre;}
  if (strcmp(flag, "-a") == 0) {field = Actor;}
  char* filename = argv[2];
  LinkedList movie_list  = ReadFile(filename);
  Index index; // TODO(Student): Create this properly. 
  index = BuildMovieIndex(movie_list, field);
  PrintReport(index);
  DestroyLinkedList(movie_list, &DestroyNothing);
  DestroyIndex(index);

  return 0;
}
