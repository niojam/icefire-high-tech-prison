# Icefire-high-tech-prison

## Task:

Escape high-tech prison by changing the KeyCardParser class only. Next conditions should be followed. 

1) Parsing your keycard data still returns your name.
2) Name does not appear in the code.
3) Name does not appear in logs.

## Solution:

First, I create Person class object when the method `read()` is invoked. Using reflection api I get static method
`getCellFor()` and invoke it with parameter person. It returns us PrisonRoom class object prisonRoom where prisoner 
has access because it is prisoner´s  personal cell. If my name generated custom hashcode (Person Class `hashcode()`) equals
with current person´s hashcode then i use `grantAccessToEveryRoom()` method.
 
`grantAccessToEveryRoom()` uses Breadth First Search algorithm to efficiently walk through all cells by getting each cell neighbours.
Access to each cell is granted using reflection api. I get private field value and make it accessible. Then the new HashSet
is made because 1) Initial HashSet is `unmodifiableSet`  2) To avoid my name appearing in logs I decided to override new HashSet 
`toString()` method. Then add all initial people who had access to new HashSet and person object with my name. Using reflection api
set `PrisonRoom` field `allowedPersons` new value which contains me. Then all collections which contains data of "hacked" 
cells are cleared to add secureness and not to store suspicious data.

In the end `read()` method returns Person class object with correct first name and last name.

## Result

High-tech prison was hacked and at least one inmate escaped.