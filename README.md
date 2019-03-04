This is a general small program repo

The DateCalculator and DateCalculator2 are doing same job in 2 different way
The target is: 
Calculating the number of full days elapsed in between start and end dates. 
The first and the last day are considered partial days and never counted. 
Following this logic, an experiment that has run from
1972-11-07 to 1972-11-08 should return 0, because there are no fully elapsed days contained in between those dates, and 2000-01-01 to 2000-01-03 should return 1. 
The solution needs to cater for all valid dates between 1901-01-01 and 2999-12-31.

The DateCalculator is implemented in a more engineering way and DateCalculator2 is with less codes. DateCalculator is more recommened.
To make it simple test cases are all put in the same class but in actual project test cases should be seperated. 

As there is test cases inside, Junit library is required to compile in a command line. e.g. :  
javac -cp /absolute/path/to/junit-4.12.jar DateCalculator.java

After compile just run "java DateCalculator {start date} {end date}" and you will get the result. 
To run test cases in command line we may run this command : java -cp /absolute/path/to/junit-4.12.jar org.junit.runner.JUnitCore DateCalculator

IDE (eclipse or intelliJ) is recommended to compile the program and run test cases. 
