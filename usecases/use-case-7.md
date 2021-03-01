# USE CASE #7: Creating a country report
## **CHARACTERISTIC INFORMATION**

### __****Goal in Context****__

As an organisation manager I want my analysts to be able to create a country report, so that I can receive easy to read, accurate reports for a country

### __****Scope****__

Primary level

### __****Company.****__

Organization that requires reports based on population data.

### __****Level****__

Primary task.

### __****Preconditions****__

We must have all countries accessible as well as their cities, regions. Database contains up to date data on all country's populations needed for the report.

### __****Success End Condition****__

All data for country's population is available for an analyst to provide reports to the manager.

### __****Failed End Condition****__

No data is accessible.

### __****Primary Actor****__

Report Analyst

### __****Trigger****__

A request for a report is sent to the company's analysts.

## __****MAIN SUCCESS SCENARIO****__

    Organization manager requires a report on a specific country.
    Analyst specifies what country they need the population for.
    analyst extracts population information for the specified country.
    Analyst provides information to be used in a report to the manager.

### __****EXTENSIONS****__

    There isn't any data for the needed country toextract from:
        Analyst informs manager they cannot make a report for the specific country.

### __****SUB-VARIATIONS****__

None.

## __****SCHEDULE****__

### __****DUE DATE:****__
Release 1.0.0
