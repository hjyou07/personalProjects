# Problem Statement: 
# Often I have to copy some files from the remote repository
# into my local repository, most often for starter codes.
# In order to do that I need to "git pull" from "resources" folder,
# then see what directory I want to copy into my local student repository,
# then copy a specific directory into my repository.
# It is not a hard job, but it is something I found can be "automated",
# and can be very efficient if implemented and used.

# Name of command: cp -R [source] [destination]
# Example of its usage: 
# cp -R ~/cs5007/resources/lab1_bash ~/cs5007/hjyou_CS5006
# it copies lab1_bash from resources and saves it in hjyou_CS5006, my local repo.

# Here is the myScript.sh script which interacts with the user,
# and copies a specific directory user(usually me) chooses into local repository.
# usage: sh myScript.sh

# A helper function that checks the validity of the user input
# if the user input matches one of the existing directories,
# returns 0. otherwise returns 1.
checkInput() {
# shell apparently can do automatic tokenization,
# (oh my who knew?! it's like python magic!)
# and $word tokenizes each substring
# from string $var which saved the output of ls command.
  result=1
  for word in $var
    do
      if [ $dirname = $word ]; then
        # 0 = true, 1=false
        result=0
      fi
    done
}

# Changes the directory to the resources repo, and pull any updated changes.
# Then it lists all the directories available for copy,
# and prompts the user to enter the name of the directory to copy.
cd ~/cs5007/resources
git pull
echo "\nfolders under resources repo:"
ls
echo "\nPick one you want to copy"
read dirname
# this saves the output of ls command (currently in ~/resources)
# into a variable named "var"
var=$(ls)
# executes the funciton checkInput() defined above,
# and saves the result into a variable named isValidInput(either 0 or 1).
checkInput
isValidInput=$result
# while isValidInput = 1, prompt the user again for an input,
# because the input didn't match any of the directories available for copy.
while [ $isValidInput -eq 1 ]
  do
    echo "that was an invalid directory name, enter the name as it's shown"
    read dirname
    checkInput
    isValidInput=$result
  done
# if the user input is valid, copy that requested directory
# from resources to local student repo.
# Then it lists all the directories in the student repo to show the result
cp -R ~/cs5007/resources/$dirname ~/cs5007/hjyou_CS5006
cd ~/cs5007/hjyou_CS5006
echo "\nYou have copied \"$dirname\", and now your repo has the following:"
ls
