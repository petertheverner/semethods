# USE CASE #11: People living and not living in cities
## **CHARACTERISTIC INFORMATION**

### __****Goal in Context****__

As an analyst I want to view people not living in cities as well as those who are, so that I can make a clear report including all living situations

### __****Scope****__

Primary level

### __****Company.****__

Organization that requires reports based on population data.

### __****Level****__

Primary-secondary task.

### __****Preconditions****__

We must have all countries accessible as well as their cities, regions. Database contains up to date data on all country's populations needed for the report.

### __****Success End Condition****__

All data for City population is available for an analyst to provide reports to the manager.

### __****Failed End Condition****__

No data is accessible.

### __****Primary Actor****__

Project Manager

### __****Trigger****__

A request for a report is sent to the company's analysts.

## __****MAIN SUCCESS SCENARIO****__

    Organization manager requires a report on population living in and outside of a city.
    Analyst specifies the people they need the population for.
    Analyst extracts region population information of what city has the largest and smallest population, also who is outside of the cities.
    Analyst provides information to be used in a report of the city to the manager.

### __****EXTENSIONS****__

    There isn't any data to extract from:
        Analyst informs manager they cannot make a report.

### __****SUB-VARIATIONS****__

None.

## __****SCHEDULE****__

### __****DUE DATE:****__
Release 1.0.1
