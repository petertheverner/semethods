# __USE CASE #2: To view the population of countries with our own requirements__

## **CHARACTERISTIC INFORMATION**

### __****Goal in Context****__

As an Analyst I want to view the top populated countries by my own requirements so that I can make updates to my analytics reports

### __****Scope****__

Primary level

### __****Company.****__

Organization that requires reports based on population data.

### __****Level****__

Primary task.

### __****Preconditions****__

We must have all countries accessible. Database contains up to date data on all country's populations needed for the report.

### __****Success End Condition****__

A report is available for an analyst to provide to organization manager.

### __****Failed End Condition****__

No report is produced.

### __****Primary Actor****__

Report Analyst

### __****Trigger****__

A request for a country's population information is sent to the company's analysts.

## __****MAIN SUCCESS SCENARIO****__

    Organization manager requires a report on the population under specific conditions.
    Analyst specifies what context they need the population for.
    analyst extracts population information of what country's are most populated.
    Analyst provides report to manager.

### __****EXTENSIONS****__

    Population is not up to date:
        Analyst informs manager information existing isn't up to date.

### __****SUB-VARIATIONS****__

None.

## __****SCHEDULE****__

### __****DUE DATE:****__
Release 1.0.0
