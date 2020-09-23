# Linker Lab

See Canvas for details.

# Submission

Modify this readme with the following information. Copy this info to the Canvas submission. Commit and push to Github.

* Commit your hack.c file.
* Put the following in your README.md file: 
   * Name(s): ```HeeJun You```
   * How many hours did it take you to complete this lab?  
   ```< 1hr```  
   * Did you collaborate with any other students/TAs/Professors?
   ```I worked on my own```  
   * Did you use any external resources? (Cite them below)
      * https://www.ibm.com/support/knowledgecenter/en/ssw_aix_71/performance/when_dyn_linking_static_linking.html
      * http://docencia.ac.upc.edu/FIB/USO/Bibliografia/unix-c-libraries.html
   * How does breaking code into libraries like this save compile time?  
   > Source codes, when put in libraries, are combined according to their functional relativity. When the files are organized,  
   > the linking process would be faster than having to hunt down all the needed symbols scattered in the disk.
   > Also, we would physically have less files to open, which enhances the speed of the linker as well.
   * How does the linker find the code needed for our executable? Does all of the code live together in one executable?  
   > How the linker finds the needed source code and output executables depend on the type of the libraries.  
   > When we use static libraries, all object files in it get inserted in the executable during the linking phase.  
   > So the executable physically contains all the files, hence bigger in size, but only one executable file is needed  
   > in order to run the program - all the needed info is in that single executable.  
   > However, since we have to compile all the object files into one single executable, if anything changes anywhere,  
   > we have to compile the whole thing all over again.  
   > Shared libraries increase the modularity as it allows for only one copy of commonly used codes,  
   > saved in a unique shared library file. The linker verifies all the symbols required during the linking phase,  
   > but doesn't physically "insert" the object files into the executable.  We need to separately provide the path to the library
   > during runtime(trade-off), but it saves a lot of disk space by reducing the size of executables,  
   > and allow modularity by keeping a certain reusable source code into a specific shared library.  
   > Also, because the libraries are loaded at runtime, any changes in a specific library can be kept as it is  
   > without having to recompile the executable every time a change is made.  
   * (Optional) What was your favorite part of the assignment?
   > I didn't realize how easy it is to "hack" a system just by exploiting the mechanism of linker with dynamic libraries!
   * (Optional) How would you improve the assignment?
