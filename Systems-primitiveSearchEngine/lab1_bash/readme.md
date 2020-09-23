# lab1_bash
- Name(s): **HeeJun You**
- How many hours did it take you to complete this lab?  
  It took an hour we were given in class to come up with an idea of what  
we want to do for this bash tutorial and implement a very simple version of it.  
I did have to research outside of class for about two hours to find what I  
wanted to implement, then the rest was just cleaning up and formatting it right.  
- Did you collaborate with any other students/TAs/Professors?  
  For this lab, I collaborated with PanPan, and we did ask a lot of questions  
to Professor Adrienne, to confirm if our idea was viable.  
- Did you use any external resources?
  - https://linuxhint.com/30_bash_script_examples/#t5
  - https://stackoverflow.com/questions/5382712/bash-how-to-tokenize-a-string-variable
- (Optional) What was your favorite part of the assignment?  
  I could actually use this script as part of my routine now when I have to copy
some starter code from the resources folder, without having to use all the "technical" commands!  
I only have to tell this interactive script what directory I want to copy.
- (Optional) How would you improve the assignment?  
  I do want to incorporate some ways to check if there's any updates or changes that has been made  
to the directory I copied, and copy the modified files only. I did find cp -u command, but that  
can't really be used alongside of cp -R so I just left this at that.

## Logistics

For this assignment (and every assignment/lab), you must login into the servers through `your_khoury_name@login.khoury.neu.edu` to complete and test your work. The reason is the examples I will provide below are compiled strictly for our machines architecture, and this is a consistent architecture where your submission will be graded.

## Automate (some of) your life

Getting familiar with the terminal is a fundamental skill to have. Many of you have various levels of experience using the terminal.

For this assignment, you will be warming up by writing a 1-2 page tutorial on bash scripting. Your tutorial will be embededded in a shell script, explaining how to do something on the terminal that is non-trivial but that can be automated. You will run your script by typing `sh myScript.sh` on the command line as an executable shell script(sh is the GNU Bourne-Again SHell -- type 'man sh' for more information).  You can look at example 'sh' scripts for inspiration on what you can achieve on the web and cite them appropriately. 

The tutorial will be done purely in text and and within a file called 'myscript.sh'. You are required to type your tutorial using a terminal based text editor like 'vim', 'emacs', or 'nano' to write your tutorial (I personally use the vim text editor).

Examples of a non-trivial scripts

* Sort a particular column of data from a .csv file (e.g. stock prices). Then print out the top ten values, and the average of that column of data.
* Write a shell script that allows you to query information about your computer system from a menu of options. Users might like to know information like hard drive space, if a graphics card is available, how fast the cpu is, etc.
* Write a shell script that downloads a webpage (using wget, perhaps to grab some json data), then parses some of the data, and prints it to the terminal. 
* You can use your imagination to create other examples.

## Part 1 - myScript.sh

<img width="400px" src="https://github.ccs.neu.edu/cs5007seaSpr2020/hjyou_CS5006/blob/master/lab1_bash/ezgif.com-video-to-gif.gif">

### Very Brief Example Tutorial (Template)
(Note this example is fairly trivial, probably worth a C-. You can use it as a template)

```
# Problem Statement: "Often I have to search for different files and directories within my computer system. 
#                     In order to do this I use the 'ls' command to help orient myself.
# Name of command: ls
# Example of its usage: 
# -bash-4.2$ ls
# Lab1  README.md

# Here is the myScript.sh script that can list the directories and how many total directories are in the current folder.
# usage: sh myScript.sh

ls
ls | wc -l
```

Some ideas for improving the script above script to earn closer to an A are the following:

- Print out a 'tree' visualization of the directory you are searching.
- Output the amount of file space each directory is taking up
- Output some graphical patterns after running the command.
- List the history of the last 100 commands that have been run.
  - Then sort how frequently they are used.

* Some inspiration for developing shell scripting skills can be found here: https://www.hackerrank.com/domains/shell


## Resources to help

- A pretty good introduction to shell scripting is here: https://www.howtogeek.com/67469/the-beginners-guide-to-shell-scripting-the-basics/ .

## Rubric

* 30% Was your tutorial 1-2 pages (estimate this as 2 regular pieces of printed paper)
    * (If you decided you were doing something so cool it had to be longer, was this well justifed)
    * If your tutorial is your actual script with comments in it, then it should be able to run. Otherwise, you should also have a script file that can actually be run accompanying your tutorial.
* 30% Polish
    * Was the tutorial well polished, did something useful, and did not contain any bugs?
* 30% Presentation
    * Was the writing presented in a clear manner in your own voice (i.e. I should not be able to Google it).
  * (Please indicate in your submission at the top if these tutorials can be shared on our piazza board)
  * (The very brief tutorial example I gave would probably earn a C- at best :) Go beyond!)
* 10% Take a screenshot of your virtual environment running. Name it 'linux.jpg'
  * Make sure to 'commit' this file to the repo in this directory.

* Note: Your code **must** run on the Khoury machines. That is where you should test it, and this is where we grade it!
* Note: You must also commit any additional files into your repository so we can test your code.
  * Points will be lost if you forget!

## Feedback Loop

(An optional task that will reinforce your learning throughout the semester)

Read this article and implement the little 'whatis' command in your .bashrc. https://lifehacker.com/how-can-i-quickly-learn-terminal-commands-1494082178

```
echo "Did you know that:"; whatis $(ls /bin | shuf -n 1)
```

This is a nice way to learn a new command everytime you log on!
