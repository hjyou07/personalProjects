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
#include <sys/stat.h>
#include <dirent.h>

#include "FileCrawler.h"
#include "DocIdMap.h"
#include "LinkedList.h"


void CrawlFilesToMap(const char *dir, DocIdMap map) {
  // STEP 3: Implement the file crawler.
  // Use namelist (declared below) to find files and put in map.
  // Be sure to lookup how scandir works. Don't forget about memory use.
  struct stat s;
  struct dirent **namelist;
  // namelist is a pointer to an array of struct dirent pointers.
  // namelist will point to an (allocated) array of pointers
  // to (allocated) strings. It includes . and .. as first two elements
  // which represents current and parent dir.
  int n;
  n = scandir(dir, &namelist, 0, alphasort);
  char sub_dir[100];
  const char* curr_dir = ".";
  const char* parent_dir = "..";
  const char* file_separator = "/";

  for (int i = 0; i < n; i++) {
    // S_ISDIR(s.st_mode) to determine if a file is a directory
    // before that, I need to concatenate the file dir to namelist[i]->d_name
    if ((strcmp(namelist[i]->d_name, curr_dir) != 0)
          && (strcmp(namelist[i]->d_name, parent_dir))) {
    // this if statement is just to get rid of . and .. in the namelist array
      // copy current directory's path into a temp string array sub_dir
      strcpy(sub_dir, dir);
    } else {
      free(namelist[i]);
      continue;
    }
    // if the passed on dir doesn't end with a file separator
    // if I manually evaluate if (strcmp() == 1), it gives an error..
    // but it works when I just say if (strcmp()) and I GUESS it works
    // because int 1 is boolean true but WHAT THE HECK?
    if (strcmp(file_separator, &dir[strlen(dir) - 1])) {
      strcat(sub_dir, "/");
    }
    // concatenate the sub directory's path to sub_dir
    // which kept track of the current directory's path
    strcat(sub_dir, namelist[i]->d_name);
    // above was all to provide a valid path for stat function
    stat(sub_dir, &s);

    // Base case: it is a file, put it in the DocIdMap
    if (S_ISREG(s.st_mode)) {
      int name_length = strlen(sub_dir);
      char *file_path = (char*)malloc(sizeof(char)*(name_length + 1));
      strcpy(file_path, sub_dir);
      // now pass on the malloced file_path and put the file in map
      PutFileInMap(file_path, map);
      // I walked through a particular namelist[i] so free it
      free(namelist[i]);
    }
    // Recursive case: it is a directory, keep finding path for the files
    if (S_ISDIR(s.st_mode)) {
      CrawlFilesToMap(sub_dir, map);
      free(namelist[i]);
    }
  }
  free(namelist);
}
