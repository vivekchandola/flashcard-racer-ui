# Flashcard-racer

# Installation 
## Windows
1. Get the `activator` downloaded from here: `https://www.lightbend.com/activator/`
2. Unzip the downloaded file
3. Add the activator in the windows `PATH`. ```(path+= ACTIVATOR_HOME\bin;)```
4. Download the source code from Github (`develop`) (https://github.com/EricKoppel/flashcard-racer.git)`
5. Navigate to source code folder `(PATH_PROVIDED_IN_STEP_4\flashcard-racer)`

## Linux
1. Get the `activator` downloaded from here: https://www.lightbend.com/activator/download
2. Unpack the downloaded file
3. Add the `activator` in to your `PATH`
4. Retrieve the latest version (The source is found in the `flascard-racer` folder).

 ```
git pull git@github.com:EricKoppel/flashcard-racer.git
git checkout develop
 ```
 
 

## Running the application
1. Execute run-command  (`activator run`), which will run the application in port `9000` (By default)
2. Navigate to browser and use localhost with port `9000`.

The following commands are used to run and test the application, to list all tasks use the `tasks`.

```
activator run
```
```
activator test
```
```
activator tasks 
 ```