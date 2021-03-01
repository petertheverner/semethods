# USE CASE #5: Extracting specific region information

## **CHARACTERISTIC INFORMATION**

### __****Goal in Context****__

As an Analyst I want to view a specific region of a country I have chosen for any requirement in regard to that region population, so that I can give accurate reports in regard to each world region

### __****Scope****__

Primary level

### __****Company.****__

Organization that requires reports based on population data.

### __****Level****__

Primary-Secondary task.

### __****Preconditions****__

We must have all countries accessible as well as their cities, regions. Database contains up to date data on all country's populations needed for the report.

### __****Success End Condition****__

All data for region population is available for an analyst to provide reports to the manager.

### __****Failed End Condition****__

No data is accessible.

### __****Primary Actor****__

Report Analyst

### __****Trigger****__

A request for a report is sent to the company's analysts.

## __****MAIN SUCCESS SCENARIO****__

    Organization manager requires a report on the population of region.
    Analyst specifies what context they need the population for.
    analyst extracts population information of what region has the largest population.
    Analyst provides information to be used in a report to the manager.

### __****EXTENSIONS****__

    There isn't any data to extract from:
        Analyst informs manager they cannot make a report.

### __****SUB-VARIATIONS****__

None.

## __****SCHEDULE****__

### __****DUE DATE:****__
Release 1.0.0
