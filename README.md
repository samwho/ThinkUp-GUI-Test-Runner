# ThinkUp GUI Test Runner
by Sam Rose

Hello! Welcome to the ThinkUp GUI Test Runner. This application was designed as a tool to
aid developers when running tests in the ThinkUp testing framework.

The application in its current form is very basic and has a lot of potential.

## INSTALLATION

The only thing that needs to be done to install this is to edit the config.txt file to point at your
ThinkUp install directory.

## ENVIRONMENT VARIABLES

It sets an extra environment variable when running tests: TEST_GUI=1. This is to let tests know
that they are being run from the GUI.

## USAGE

To use the program you currently need to cd into the main directory directory and run java -jar dist/SimpleTestGUI.jar
from there.

To run tests, simply select a test file from the first list then select one or more test methods from
the second list and hit enter or click run.

## EDITING

The GUI for this program was generated using the NetBeans GUI editor. It's advisable to edit this program
in NetBeans if you plan on messing with the GUI :)