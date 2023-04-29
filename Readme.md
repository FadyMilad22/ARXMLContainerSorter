ARXML Container Sorter
This program reads an AUTOSAR XML file containing a list of containers, each with a unique ID, and reorders the containers alphabetically by their "SHORT-NAME" sub-element. The sorted containers are written to a new AUTOSAR XML file.

Done by :
Fady Milad Garas Habib
Id: 1900003

attached in the src folder :
3 test files to test each case 
1 Batch file can be excuted in the commandline 
3 classes files
 
Usage
To run the program, use the Batchfile

Where "arxmlfile.arxml" is the name of the AUTOSAR XML file to be sorted. The sorted containers will be written to a new file called "arxmlfile_mod.arxml" in the same directory as the input file.

If the input file is not a valid AUTOSAR file (i.e. does not have a .arxml extension), a "NotValidAutosarFileException" will be thrown.

If the input file is empty, an "EmptyAutosarFileException" will be thrown.

Exceptions
The program may throw the following exceptions:

NotValidAutosarFileException: thrown when the input file is not a valid AUTOSAR file.
EmptyAutosarFileException: thrown when the input file is empty.