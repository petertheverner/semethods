# USE CASE #10: Creation of reports with percentages 
## **CHARACTERISTIC INFORMATION**

### __****Goal in Context****__

As an organisation manager I want my analysts to be able to create a population report with percentages, so that I can receive easy to read, accurate reports for populations of different areas

### __****Scope****__

Primary level

### __****Company.****__

Organization that requires reports based on population data.

### __****Level****__

Primary task.

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

    Organization manager requires a report on population of an area using percentages.
    Analyst specifies the area they need the population for.
    Analyst extracts population information in largest and smallest population.
    Analyst provides information to be used in a report of the area using percentages to the manager.

### __****EXTENSIONS****__

    There isn't any data using percentages to extract from:
        Analyst informs manager they cannot make a report using percentages and gives them a regular report instead.

### __****SUB-VARIATIONS****__

None.

## __****SCHEDULE****__

### __****DUE DATE:****__
Release 1.0.1
