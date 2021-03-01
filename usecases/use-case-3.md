# USE CASE #3: Capital city by descending and ascending order 

## **CHARACTERISTIC INFORMATION**

### __****Goal in Context****__

As an analyst I want to be able to view all the capital cities in the world in descending and ascending order by my own requirements so that I can make updates to population size, per city

### __****Scope****__

Primary level

### __****Company.****__

Organization that requires reports based on population data.

### __****Level****__

Primary-Secondary task.

### __****Preconditions****__

We must have all countries accessible as well as their cities. Database contains up to date data on all country's populations needed for the report.

### __****Success End Condition****__

A report is available for an analyst to provide to organization manager.

### __****Failed End Condition****__

No report is produced.

### __****Primary Actor****__

Report Analyst

### __****Trigger****__

A request for a capital city's population information is sent to the company's analysts.

## __****MAIN SUCCESS SCENARIO****__

    Organization manager requires a report on the population of a city for how large it is.
    Analyst specifies what context they need the population for.
    analyst extracts population information of what capital city has the largest population.
    Analyst provides information to be updated in a report to manager.

### __****EXTENSIONS****__

    Population is not up to date:
        Analyst informs manager information existing isn't up to date.

### __****SUB-VARIATIONS****__

None.

## __****SCHEDULE****__

### __****DUE DATE:****__
Release 1.0.0
