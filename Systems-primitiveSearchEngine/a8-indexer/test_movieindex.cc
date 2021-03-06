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
#include <stdint.h>
#include <assert.h>


#include "gtest/gtest.h"

extern "C" {
  #include "./MovieIndex.h"
  #include "htll/LinkedList.h"
  #include "MovieIndex.h"
  #include "Movie.h"
  #include "MovieReport.h"
  #include "FileParser.h"
  #include <string.h>
}

const char* movie_row_A = "9.3|The Shawshank Redemption|R|Crime|142|Tim Robbins,Morgan Freeman,Bob Gunton";
const char* movie_row_B = "7.4|Back to the Future Part III|PG|Adventure|118|Michael J. Fox,Christopher Lloyd,Mary Steenburgen";

void DestroyLLMovie(void *payload) {
  DestroyMovie((Movie*)payload);
}

TEST(MovieIndex, CreateDestroy) {
  Index index = CreateIndex();
  ASSERT_NE(index, nullptr);
  DestroyIndex(index);
}


TEST(MovieIndex, AddMovieToIndex) {
  // Copying the string from a const to an array
  // (to be more similar to the actual use case)
  char row[1000];
  strcpy(row, movie_row_A);
  // Take a movie
  MoviePtr m1 = CreateMovieFromRow(row);
  strcpy(row, movie_row_B);
  MoviePtr m2 = CreateMovieFromRow(row);
  // Create Index (hashtable)
  Index index = CreateIndex();
  // Add movie to index (The Shawshank Redemption indexed by ContentRating)
  AddMovieToIndex(index, m1, ContentRating);
  // Check that movie is in index
  uint64_t key = ComputeKey(m1, ContentRating);
  HTKeyValue kvp;
  HTKeyValue* kvp_pointer = &kvp;
  int set_exists = LookupInHashtable(index, key, kvp_pointer);
  ASSERT_EQ(set_exists, 0);
  // Check size/num elements (should be num of types, not of movies)
  ASSERT_EQ(NumElemsInHashtable(index), 1);

  // Try to insert movie again
  AddMovieToIndex(index, m1, ContentRating);
  // TODO: What's supposed to happen here?
  // Write code to check it.
  ASSERT_EQ(NumElemsInHashtable(index), 1);
  // Add another movie to the index (same IndexType)
  AddMovieToIndex(index, m2, ContentRating);
  ASSERT_EQ(NumElemsInHashtable(index), 2);

  // Add another movie (different IndexType)
  // TODO: Check result is correct.
  AddMovieToIndex(index, m1, Genre);
  // now that I added another movie with an obvious different key,
  // NumElemsInHashtable should be increased.
  ASSERT_EQ(NumElemsInHashtable(index), 3);
  PrintReport(index);

  // Destroy movie index
  DestroyIndex(index);
}


TEST(MovieIndex, GetMovieSet) {
  // TODO: Implement this test.

  // Create a movie index

  // Add a bunch of movies with different vals for the index

  // Get the set

  // Check that the movies are all in the set

  // Destroy the movie index

}


int main(int argc, char **argv) {
    ::testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}
