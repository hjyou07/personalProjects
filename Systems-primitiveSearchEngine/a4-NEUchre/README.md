## Summary ##
Assignment 4 is about building the card game NEUchre,  
Milestone 1 included implementing the deck as a stack, with related functions.  
Milestone 2 included implementing the rest, which are the hand and the game functions.

An empty deck is created and is populated with cards.  
You can push a card to the deck, peek at the top card in the deck, pop a card from the deck  
and also destory the deck when it's needed, which frees the memory initially allocated upon creation of the deck.

An empty hand is created and players are dealt 5 cards in their hands when the game starts.  
When they play a card, it is checked whether the card is considered a legal move,  
and when it is, the card is removed from the hand and "played".  
After both players play their cards, a winner is determined.  
If the suits are the same on the two cards, the higher card value wins,  
and otherwise, the lead player wins unless the other player played trump.  

## In a4 directory, you will find: ##  
- *deck.c*: contains all the relevant deck functions  
- *a4.h*: header files for all the functions to be implemented  
- *a4.c*: contains (will contain) all the other functions; hand and game functions  
- *a4_helpers.c*: contains the helper functions, mostly to check if the project is on the right track  
- *a4_run.c*: runs the game  
- *a4_test.c*: tests all the functionality of written (to be written) functions  
- *Makefile*: set make commands to compile and run the program  
- *README.md*: this file  
Most of the files are provided as a starter code; **deck.c** and **a4.c** are the ones that were written.  

## Heap Memory vs Stack Memory ##
This project significantly utilizes heap memory so that we have a better control of the memory allocation.  
stack memory and heap memory differ in what type of data they store.  

Data stored in **_stack_** memory is static and of quick access, and the user doesn't have control over allocation and freeing of memory.  
Hence it is more suitable for a local variable that needs quick access.  
In stack memory, the data is allocated linearly, so is stored more "in order".
The disadvantage is that it always has to find a contiguous space for memory,  
and can result in shortage of memory space.  

Data stored in **_heap_** memory is dynamic and of a slower access, but the user has control over the lifetime of the specific data.  
If the data needs to exist outside of a particular function or multiple functions,  
or even over a entire program, heap memory will be a valuable source.  
Hence it is more suitable for a global variable.  
In heap memory, the data is allocated non-linearly, as they are manipulated by pointers.  
This is both an advantage and and disadvantage to a heap memory,  
as it doesn't require a big continuous chunk of memory to store data but can cause fragmentation.

## Overall ##
this project was a good practice in getting familiar with heap memory, as we had to utilize malloc and free.  
It also provided a comprehensive understanding of everything we have been learning so far.
Different data structures are being used with different functionalities, and implementing the myself helped a lot.  
Segmentation fault was the trickiest error to resolve as they don't give any significant insight on what is wrong. I tried to minimize getting erros of "unknown" nature by writing my own mini tests as I write a function and  
testing every time I add a new branch or functionality.
