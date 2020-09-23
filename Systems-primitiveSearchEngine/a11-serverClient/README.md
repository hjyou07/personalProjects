## Final Project
### To execute
To run the server and the client, open up two shell windows so that each can run the client and the server respectively.  
With the provided makefile, these two can be compiled by just calling the command ```make server``` and ```make clienti```.
If you wish to run the multiprocessing server, run make multiserver.  
  
On one shell window, run the server first by calling ```make runserver```(or ```make runmultiserver```).
On the other shell window, run the client by calling make runclient.  
If you wish to run valgrind for memory check, do not call ```valgrind make runserver``` nor ```valgrind make runclient```.
The valgrind captures what it thinks is a leak inherent to the Makefile.  
Enter the actual execution command such as ```valgrind ./queryserver -f data_small/ -p 1500```.  
### Submission elements
- Name:```HeeJun You```
- How many hours did it take you to complete this assignment?  
```It took around 20 hours```
- Did you collaborate with any other students/TAs/Professors? (Name them below)
  - ```Discussion on MS Teams```
- Did you use any external resources? (Cite them below)
  - https://stackoverflow.com/questions/13669474/multiclient-server-using-fork
  - https://github.com/nikhilroxtomar/Multiple-Client-Server-Program-in-C-using-fork/blob/master/tcpServer.c
- (Optional) What was your favorite part of the assignment?
- (Optional) How would you improve the assignment?  
