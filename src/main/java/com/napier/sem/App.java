/* Authors:
           Liam Blair - 40437578
           Peter Verner - 40488882

   Last modified: 24/02/2020
*/
package com.napier.sem;
// Imports all SQL methods.

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class App
    {
        // Connection object initialised as null.
        private Connection con = null;

        /**
         * Method that attempts to connect to the database container. Will
         * Make up to 10 attempts until returning an error.
         */
        public void Connect(String location) {

            try
            {
                Class.forName("com.mysql.jdbc.Driver");
            }
            catch (ClassNotFoundException e)
            {
                System.out.println("SQL driver could not be loaded.");
                System.exit(-1);
            }

            int tries = 10;

            for (int i = 0; i < tries; i++) {
                System.out.println("Connecting to database...");

                try
                {
                    Thread.sleep(5000);
                    con = DriverManager.getConnection("jdbc:mysql://" + location + "/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
                    System.out.println("Successful connection to the database.");
                    System.out.println("--------------------------------------------------");
                    break;
                }

                catch (SQLException e)
                {
                    System.out.println("Failed to connect to database on attempt number " + i);
                    System.out.println(e.getMessage());
                }

                catch (InterruptedException e)
                {
                    System.out.println("System interrupted");
                }
            }
        }


        /**
         * Method that attempts to sever the connection to a connected database.
         */
        public void Disconnect()
        {
            if (con != null)
            {
                try
                {
                    con.close();
                    System.out.println("--------------------------------------------------");
                    System.out.println("Successfully terminated connection to the database");
                }
                catch (Exception e)
                {
                    System.out.println("Error terminating connection to the database.");
                }
            }
        }


        /**
         * This is the method that searches for the total population of a specific place or area.
         *
         * @param searchType int The type of search being done (Country, City, etc).
         * @param search String The name of the location used in the search.
         * @return long The total population of the search.
         */
        public long GetPopulation(int searchType, String search)
        {
            // Total stored as long to be able to output extremely large population numbers, such
            // as worldwide population
            long total = 0;

            if((search == "" && searchType != 1) || search == null)
            {
                System.out.println("Error: search type provided was greater than 1, but no search given");
                return -1;
            }
            String query;

            // Switch statement takes in the search type and uses it to determine what population search
            // is being done. For example, inputting a searchType of 2 means that a query will be built for
            // searching for continent population data.
            switch (searchType)
            {
                // World Population
                case 1:
                    query = "SELECT Population FROM country";
                    break;

                // Continent Population
                case 2:
                    query = "SELECT Population "
                            +"FROM country "
                            +"WHERE Continent = '" + search + "'";
                    break;

                // Region Population
                case 3:
                    query = "SELECT Population "
                            +"FROM country "
                            +"WHERE Region = '" + search + "'";
                    break;

                // Country Population
                case 4:
                    query = "SELECT Population "
                            +"FROM country "
                            +"WHERE Name = '" + search + "'";
                    break;

                // District Population
                case 5:
                    query = "SELECT Population "
                            +"FROM city "
                            +"WHERE District = '" + search + "'";
                    break;

                // City Population
                case 6:
                    query ="SELECT Population "
                            +"FROM city "
                            +"WHERE Name = '" + search + "'";
                    break;

                default:
                    System.out.println("Error: Incorrect search type provided.");
                    return -1;
            }

            try
            {
                // After query is built, it is executed
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery(query);

                // Loops through all retrieved rows.
                while(rset.next())
                {
                    // Population is added to the total
                    total += rset.getInt("Population");
                }
            }
            // Catches any errors that occur during the query execution and returns an appropriate
            // error message.
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Error retrieving population data for:" + search);
            }
            // Total is returned if no errors are found and the query is successfully executed.
            return total;
        }

        /** This method instantiates a country arraylist and adds countries equal to the count
         * provided by the user. Only logs country names and populations.
         *
         * @param count int The number of countries to output
         * @return Countries An arraylist of countries, stores country objects for output.
         */
        public ArrayList<City> GetCityPopulations(int count)
        {
            if(count < 1)
            {
                System.out.println("Error! Invalid count provided.");
                return null;
            }
            // i used to check count
            int i = 0;
            // Init arraylist and query
            ArrayList<City> cities = new ArrayList<City>();
            String query = "SELECT Population, Name "
                    +"FROM city "
                    +"ORDER BY Population DESC";

            try
            {
                // Execute statement
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery(query);

                // Keep adding country objects until either the entire country table has been searched or the count limited
                // is reached.
                while(rset.next() && i < count)
                {
                    City tempCity = new City();
                    tempCity.setCitypopulation(rset.getInt("Population"));
                    tempCity.setCityName(rset.getString("Name"));
                    cities.add(tempCity);
                    i++;
                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Error retrieving population data for top populated city's.");
            }

            return cities;
        }

        /** This method instantiates a City object and populates it with data relating to
         * the input City.
         *
         * @param city String The name of the city used as an input for the search
         * @return City A city object with relevant information to it's city.
         */

        public City GetCityReport(String city)
        {
            City tempCity = new City();
            String countryCode = "";
            String query = "SELECT Name, Population, District, CountryCode "
                    +"FROM city "
                    +"WHERE Name = '" + city + "'";
            try
            {
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery(query);

                if(rset.next())
                {
                    tempCity.setCityName(rset.getString("Name"));
                    tempCity.setCitypopulation(rset.getInt("Population"));
                    tempCity.setCitydistrict(rset.getString("District"));
                    countryCode = rset.getString("CountryCode");


                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Error retrieving the report for the city " + city);
                return null;
            }
            try {
                query = "SELECT Name, Code "
                        +"FROM country "
                        +"WHERE Code='" + countryCode + "'";
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery(query);
                if(rset.next())
                {
                    tempCity.setCityCountry(rset.getString("Name"));
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Error retrieving the report for the city " + city);
            }
            return tempCity;
        }



        /** This method instantiates a country object and populates it with relating to
         * the input country.
         *
         * @param country String The name of the country used as an input for the search
         * @return Country A country object with relevant information to it's country.
         */
        public Country GetCountryReport(String country)
        {
            // init country
            Country tempCountry = new Country();

            // Check if input is null or empty
            if(country == null || country == "")
            {
                System.out.println("Error: country input is empty.");
                tempCountry.setPopulation(-1);
                return tempCountry;
            }
            int capitalSearch = 0;
            String query = "SELECT Code, Name, Continent, Region, Population, Capital "
                    +"FROM country "
                    +"WHERE Name = '" + country + "'";

            try
            {
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery(query);

                if(rset.next())
                {
                    tempCountry.setName(rset.getString("Name"));
                    tempCountry.setCode(rset.getString("Code"));
                    tempCountry.setContinent(rset.getString("Continent"));
                    tempCountry.setRegion(rset.getString("Region"));
                    capitalSearch = rset.getInt("Capital");
                    tempCountry.setPopulation(rset.getInt("Population"));
                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Error retrieving the report for the country " + country);
                return null;
            }


            try
            {
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery("SELECT Name FROM city WHERE id = '" + capitalSearch + "'");
                if(rset.next())
                {
                    tempCountry.setCapital(rset.getString("Name"));
                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Error retrieving the capital city from the country " + country);
                return null;
            }
            return tempCountry;
        }

        /** This method instantiates a country arraylist and adds countries equal to the count
         * provided by the user. Only logs country names and populations.
         *
         * @param count int The number of countries to output
         * @return Countries An arraylist of countries, stores country objects for output.
         */
        public ArrayList<Country> GetCountryPopulations(int count)
        {
            // init array list
            ArrayList<Country> Countries = new ArrayList<Country>();

            // Check if count input is negative or 0
            if(count <= 0)
            {
                System.out.println("Error: count was either 0 or negative.");
                return Countries;
            }
            // i used to check count
            int i = 0;
            // Init query
            String query = "SELECT Population, Name "
                    +"FROM country "
                    +"ORDER BY Population DESC";

            try
            {
                // Execute statement
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery(query);

                // Keep adding country objects until either the entire country table has been searched or the count limited
                // is reached.
                while(rset.next() && i < count)
                {
                    Country tempCountry = new Country();
                    tempCountry.setPopulation(rset.getInt("Population"));
                    tempCountry.setName(rset.getString("Name"));
                    Countries.add(tempCountry);
                    i++;
                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Error retrieving population data for top populated countries.");
            }

            return Countries;
        }

        /** This method instantiates a country object and populates it with relating to
         * the input country.
         *
         * @param region String The name of the region used as an input for the search
         * @return Region An object with relevant information to it's region.
         */
        /*
        public Region GetRegionReport(String region)
        {
            Region tempRegion = new Region();
            int RegionNameSearch = 0;
            String query = "SELECT Name, Population, CountryCode "
                    +"FROM region "
                    +"WHERE Name = '" + region + "'";
            try
            {
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery(query);

                if(rset.next())
                {
                    tempRegion.setRegionName(rset.getString("Name"));
                    tempRegion.setRPopulation(rset.getInt("Population"));
                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Error retrieving the report for the region " + region);
                return null;
            }
            return tempRegion;
        }*/

        /** This method instantiates a country arraylist and adds countries equal to the count
         * provided by the user. Only logs country names and populations.
         *
         * @param count int The number of regions to output
         * @return Regions An arraylist of regions, stores region objects for output.
         **/

/*
        public ArrayList<Region> GetRegionPopulations(int count)
        {
            // init array list
            ArrayList<Region> Regions = new ArrayList<Region>();

            // Check if count input is negative or 0
            if(count <= 0)
            {
                System.out.println("Error: count was either 0 or negative.");
                return Regions;
            }
            // i used to check count
            int i = 0;
            // Init query
            String query = "SELECT Name ,Population, Region "
                    +"FROM Country "
                    +"ORDER BY Population DESC";

            try
            {
                // Execute statement
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery(query);

                // Keep adding Regions objects until either the entire country table has been searched or the count limited
                // is reached.
                while(rset.next() && i < count)
                {
                    Region tempRegion = new Region();
                    tempRegion.setRegionCountry(rset.getString("Regions Country"));
                    tempRegion.setRPopulation(rset.getInt("Population"));
                    tempRegion.setRegionName(rset.getString("Region Name"));
                    Regions.add(tempRegion);
                    i++;
                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Error retrieving population data for top populated countries.");
            }

            return Regions;
        }
*/

        /** This method instantiates a city arraylist and retrieves capital cities from
         * all countries in the world
         *
         * @return Cities An arrayList of capital cities
         **/
        public ArrayList<City> GetCapitalCities()
        {
            ArrayList<City> Cities = new ArrayList<City>();

            // As the cities table does not record any information on cities being capital, all capital city
            // codes need to be recorded from the Country table.
            String query = "SELECT Capital "
                           +"FROM country ";
            ArrayList<Integer> CapitalCodes = new ArrayList<Integer>();

            try
            {
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery(query);

                while(rset.next())
                {
                    // Record each capital code, since every country has a capital, no validation checking is needed.
                    CapitalCodes.add(rset.getInt("Capital"));
                }
            }

            catch (Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Unable to retrieve country capital cites.");
            }

            // For each capital city code found, execute a query to find the city with that code
            // in the city table.
            for(int i = 0; i < CapitalCodes.size(); i++)
            {
                query = "SELECT Name, Population "
                        +"FROM city "
                        +"WHERE ID = " + CapitalCodes.get(i);

                try
                {
                    Statement stmt = con.createStatement();
                    ResultSet rset = stmt.executeQuery(query);

                    // If statement used as theres only 1 ID per capital city. Once the city is found,
                    // it will proceed to the next iteration of the for loop.
                    if(rset.next())
                    {
                        City tempCity = new City();
                        tempCity.setCitypopulation(rset.getInt("Population"));
                        tempCity.setCityName(rset.getString("Name"));

                        Cities.add(tempCity);
                    }
                }

                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                    System.out.println("Unable to retrieve and/or record capital city names and populations.");
                }
            }

            return Cities;
        }

        /** This method generates a report on a capital city. The method will also check that
         * the input city is a capital city.
         *
         * @param city String The name of the city
         * @return City A populated city object
         **/
        public City getCapitalCityReport(String city)
        {
            // Init variables to retrieve country code and the ID of the city
            int cityID = -1;
            int countryCode = -1;
            City TempCity = new City();

            // Check that a correct is input is given
            if(city == null || city == "" )
            {
                System.out.println("Error: Invalid city provided!");
                return null;
            }

            // Setup initial query to get simple city data
            String query = "SELECT ID, Name, District, Population, CountryCode "
                    +"FROM city "
                    +"WHERE Name='" + city + "'";

            try
            {
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery(query);

                // Set city's population and district. Also record the city ID and country code for future use.
                if(rset.next())
                {
                 TempCity.setCityName(city);
                 cityID = rset.getInt("ID");
                 TempCity.setCitydistrict(rset.getString("District"));
                 TempCity.setCitypopulation(rset.getInt("Population"));
                }

                // Error message generated if city not found.
                else
                {
                    System.out.println("Error! City not found!");
                }
            }

            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }

            // Generate 2nd query, to confirm that the input city is a capital by comparing the cityID to capital city IDs of all countries.
            query = "SELECT Capital, Code, Name "
                    +"FROM country "
                    +"WHERE Capital='" + cityID + "'";

            try
            {
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery(query);
                // If cityID matches and Country Code matches, populate the rest of the city object and return it.
                if(rset.next())
                {
                    if(cityID == rset.getInt(("Capital")))
                    {
                        TempCity.setCityCountry(rset.getString("Name"));
                        return TempCity;
                    }
                    // Return appropriate error if there is no matches, AKA the city is not a capital city.
                    else
                    {
                        System.out.println("Error! Input city is not a capital city!");
                    }
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
            return null;
        }

        //Currently unused

        public Languages GetLanguageReport(String languages) //(use case 13)
        {
            // init language with a country code to find a country and how many langauges there are there/ the most spoken
            int countryCode = -1;
            Languages tempLanguage = new Languages();

            // Check if input is null or empty
            if(languages == null || languages == "")
            {
                System.out.println("Error: language input is empty.");
                tempLanguage.setPercentage(-1);
                return tempLanguage;
            }
            int languageSearch = 0;
            String query = "SELECT CountryCode , Language, IsOfficial, Percentage "
                    +"FROM countrylanguage "
                    +"WHERE Name = '" + languages + "'";

            try
            {
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery(query);


                if(rset.next())
                {
                    tempLanguage.setLanguageCCODE(rset.getInt("Code"));
                    tempLanguage.setLanguage_Name(rset.getString("Language Name"));
                    tempLanguage.setIsofficial(rset.getString("Official Language"));
                    tempLanguage.setPercentage(rset.getInt("Percentage of speakers %"));
                    languageSearch = rset.getInt("Capital");

                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Error retrieving the report for the language Spoken " + languages);
                return null;
            }


            try
            {
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery("SELECT language FROM Language id = '" + languageSearch + "'");
                if(rset.next())
                {
                    tempLanguage.setPercentage(rset.getInt("Number of Language Speakers"));
                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Unable to retrieve country language statistics ");
                return null;
            }
            return tempLanguage;
        }


        public ArrayList<Languages> GetLanguagePopulations(int count)
        {
            // init array list
            ArrayList<Languages> Language = new ArrayList<Languages>();

            // Check if count input is negative or 0
            if(count <= 0)
            {
                System.out.println("Error: count was either 0 or negative.");
                return Language;
            }
            // i used to check count
            int i = 0;
            // Init query
            String query = "SELECT Language, Percentage, IsOfficial "
                    +"FROM countrylanguage "
                    +"ORDER BY Percentage DESC";

            try
            {
                // Execute statement
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery(query);

                // Keep adding language objects until either the entire countrylanguage table has been searched or the count limited
                // is reached.
                while(rset.next() && i < count)
                {
                    Languages tempLanguage = new Languages();
                    tempLanguage.setLanguage_Name(rset.getString("Language"));
                    tempLanguage.setPercentage(rset.getInt("Percentage"));
                    tempLanguage.setIsofficial(rset.getString("IsOfficial"));
                    Language.add(tempLanguage);
                    i++;
                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Error retrieving population data for top populated countries.");
            }

            return Language;
        }

        public long GetLanguagePopulation(int searchType, String search)
        {
            // Total stored as long to be able to output extremely large population numbers, such
            // as worldwide population
            long Ltotal = 0;

            if((search == "" && searchType != 1) || search == null)
            {
                System.out.println("Error: search type provided was greater than 1, but no search given");
                return -1;
            }
            String query;

            // Switch statement takes in the search type and uses it to determine what language population search
            // is being done. For example, inputting a searchType of 2 means that a query will be built for
            // searching for continent population data.
            switch (searchType)
            {
                // English Population
                case 1:
                    query = "SELECT Percentage FROM countrylanguage"
                             +"FROM countrylanguage "
                             +"WHERE Language = 'English'" + search;
                    break;

                // Hindi Population
                case 2:
                    query = "SELECT Percentage FROM countrylanguage"
                            +"FROM countrylanguage "
                            +"WHERE Language = 'Hindi'" + search;
                    break;

                // Spanish Population
                case 3:
                    query = "SELECT Percentage FROM countrylanguage"
                            +"FROM countrylanguage "
                            +"WHERE Language = 'Spanish'" + search;
                    break;

                // Arabic Population
                case 4:
                    query = "SELECT Percentage FROM countrylanguage"
                            +"FROM countrylanguage "
                            +"WHERE Language = 'Arabic'" + search;
                    break;

                // Chinese Population
                case 5:
                query = "SELECT Percentage FROM countrylanguage"
                        +"FROM countrylanguage "
                        +"WHERE Language = 'Chinese'" + search;
                    break;

                default:
                    System.out.println("Error: Incorrect search type provided.");
                    return -1;
            }

            try
            {
                // After query is built, it is executed
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery(query);

                // Loops through all retrieved rows.
                while(rset.next())
                {
                    // Population is added to the total
                    Ltotal += rset.getInt("Percentage");
                }
            }
            // Catches any errors that occur during the query execution and returns an appropriate
            // error message.
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Error retrieving percentage data for:" + search);
            }
            // Total is returned if no errors are found and the query is successfully executed.
            return Ltotal;
        }

        /** This method returns the combined popoulation of all cities within a given continent,
         * country, or region.
         *
         * @param searchType int The type of area the search is being done on (region, country or continent).
         * @param area String the name of the location to search.
         * @return long A number representing the total combined city populations present in the area.
         **/
        long GetAllCityPopulations(int searchType, String area)
        {
            String query;
            // Variables: cityPops stores collective population. Countries stores names of country codes.
            long cityPops = -1;
            ArrayList<String> Countries = new ArrayList<>();
            if(searchType < 1 || searchType > 3)
            {
                System.out.println("Error! searchType out of range!");
                return -1;
            }

            // Create initial structure of the query.
            query = "SELECT Code "
                    +"FROM country "
                    +"WHERE ";

            // Assemble end of query by searchType. 1 = continent, 2 = country, 3 = region.
            // Used to collect all cities within these areas to then get city populations.
            switch (searchType)
            {
                case 1: query += "Continent='" + area + "'"; break;
                case 2: query += "Name='" + area + "'"; break;
                case 3: query+= "Region='" + area + "'"; break;
                default: return -1;
            }

            try
            {
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery(query);

                while(rset.next())
                {
                    Countries.add(rset.getString("Code"));
                }
            }
            catch(Exception e)
            {
                System.out.println("Error assembling list of countries!\n" + e.getMessage());
                return -1;
            }

            for(int i = 0; i < Countries.size(); i++)
            {
                query = "SELECT Population "
                        +"FROM city "
                        +"WHERE CountryCode ='" + Countries.get(i) + "'";
                try
                {
                    Statement stmt = con.createStatement();
                    ResultSet rset = stmt.executeQuery(query);

                    while(rset.next())
                    {
                        cityPops += rset.getInt("Population");
                    }
                }
                catch(Exception e)
                {
                    System.out.println("Error retrieving population for a city!\n" + e.getMessage());
                }
            }
            return cityPops;
        }


        public static void main(String[] args)
        {
            // Initialise the application
            App a = new App();

            // Connect to the database
            if(args.length < 1)
            {
                a.Connect("localhost:3306");
            }
            else
            {
                a.Connect(args[0]);
            }

            // Print populations of the world, a continent, a region, a country, a district and a city (Use Case 04)
            System.out.println("----- POPULATIONS - Use Case 04 -----");

            System.out.println("\nPopulation of the world");
            System.out.println(">> " + String.format("%,d",a.GetPopulation(1, "")) + " <<");
            System.out.println("Population of a continent (Europe)");
            System.out.println(">> " + String.format("%,d", a.GetPopulation(2, "Europe")) + " <<");
            System.out.println("Population of a region (British Islands)");
            System.out.println(">> " + String.format("%,d", a.GetPopulation(3, "British Islands")) + " <<");
            System.out.println("Population of a country (Great Britain)");
            System.out.println(">> " + String.format("%,d", a.GetPopulation(4, "United Kingdom")) + " <<");
            System.out.println("Population of a district (Scotland)");
            System.out.println(">> " + String.format("%,d", a.GetPopulation(5, "Scotland")) + " <<");
            System.out.println("Population of a city (Edinburgh)");
            System.out.println(">> " + String.format("%,d", a.GetPopulation(6, "Edinburgh")) + " <<");


            // Print report of a specified country (Use Case 07)
            System.out.println("\n----- COUNTRY REPORT - Use Case 07 -----");
            System.out.println("\n Country report of the country 'Denmark' : ");
            // Calls the GetCountryReport method which returns a country object. Then it calls
            // the toString method of the Country class which returns a text output.
            System.out.println(a.GetCountryReport("Denmark").toString());


            // Print report of a specified city (Use Case 08)
            System.out.println("\n----- CITY REPORT - Use Case 08 -----");
            System.out.println("\n City report of the city 'Edinburgh' : ");
            // Calls the GetCountryReport method which returns a country object. Then it calls
            // the toString method of the Country class which returns a text output.
            System.out.println(a.GetCityReport("Edinburgh").toString());


            // Print N most populated countries in the world (Use Case 02).
            System.out.println("\n----- COUNTRY POPULATION BY NUMBER - USE CASE 2 -----");
            System.out.println("\nTop 10 most populated countries : ");
            // Init array for output
            ArrayList <Country> CountryList = new ArrayList<Country>();
            // Retrieve array
            CountryList = a.GetCountryPopulations(10);
            Country aCountry;
            // Output and getting country reports
            for(int i = 0; i < 10; i++)
            {
                aCountry = a.GetCountryReport(CountryList.get(i).getName());
                System.out.println(aCountry.toString() + "\n");
            }


            // Print different languages  populations out for the English, Hindi, Spanish, Arabic and Chinese
            // (Use Case 13)
            System.out.println("\n----- LANGUAGE REPORT - Use Case 13 -----");
            System.out.println("\n Language report for the English, Chinese, Hindi, Spanish and Arabic Language : ");
            // Init array for output
            ArrayList <Languages> Language = new ArrayList<Languages>();
            // Retrieve array
            // Output
            for(int i = 0; i < 10; i++)
                System.out.println("\nPopulation of English Language Speakers in the World" + "");
            System.out.println(">> " + String.format("%,d",a.GetLanguagePopulation(1, "English")) + " <<");
            System.out.println("Population of Hindi Speakers in the World");
            System.out.println(">> " + String.format("%,d", a.GetLanguagePopulation(2, "Hindi")) + " <<");
            System.out.println("Population of Spanish Speakers in the World");
            System.out.println(">> " + String.format("%,d", a.GetLanguagePopulation(3, "Spanish")) + " <<");
            System.out.println("Population of Arabic Speakers in the World");
            System.out.println(">> " + String.format("%,d", a.GetLanguagePopulation(4, "Arabic")) + " <<");
            System.out.println("Population of Chinese Speakers in the World");
            System.out.println(">> " + String.format("%,d", a.GetLanguagePopulation(5, "Scotland")) + " <<");

            // Print N most populated city's in the world or continent (Use Case 14).
            System.out.println("\n----- CITY POPULATION BY NUMBER - USE CASE 14 -----");
            System.out.println("\nTop 10 most populated City's in the world : ");
            // Init array for output
            City aCity;
            ArrayList <City> CityList;
            // Retrieve array
            CityList = a.GetCityPopulations(10);
            // Output
            System.out.println("TOP 10 MOST POPULATED CITY'S : ");
            for(int i = 0; i < 10; i++)
            {
                aCity = a.GetCityReport(CityList.get(i).getCityName());
                System.out.println(aCity.toString() + "\n");
            }

            //All countries in a continent organised by largest to smallest population (use case


            // Print population of people not living in cities in continents (Use Case 11)
            System.out.println("\n----- PEOPLE NOT LIVING IN CITIES IN CONTINENTS - USE CASE 11");
            // Initialise arrays to store populations, and to store continent data so a for loop can be used.
            // Also initialises a percentage variable to calculate % of population living in a city for the output.
            String[] continents = {"Asia", "Europe", "North America", "Africa"};
            long[] continentPops = {0, 0, 0, 0};
            long[] cityPops = {0, 0, 0, 0};
            double percentInCity;
            double percentNotInCity;

            // For each iteration, get a continent's population, find the collective city populations
            // inside the continent, and then find the number of people not living in cities, and percentage.
            for(int i = 0; i < 4; i++)
            {
                continentPops[i] = a.GetPopulation(2, continents[i]);
                cityPops[i] = a.GetAllCityPopulations(1, continents[i]);

                percentInCity = ((float)cityPops[i] / (float)continentPops[i]) * 100;
                percentNotInCity = (((float)continentPops[i] - (float)cityPops[i]) / (float)continentPops[i]) * 100;

                System.out.println(continents[i] + " : ");
                System.out.println("People living in the continent : " + String.format("%,d", continentPops[i]));
                System.out.println("People living in cities : " + String.format("%,d", cityPops[i]) + " ( " + percentInCity + "% )");
                System.out.println("People not living in cities : " + String.format("%,d", (continentPops[i] - cityPops[i])) + " ( " + percentNotInCity + "% )");
            }


            // Print all capital cities by population (Use Case 03).
            System.out.println("\n----- CAPITAL CITIES BY POPULATION - USE CASE 03 -----");
            ArrayList<City> CitiesPopulations = new ArrayList<City>();
            // Populate arraylist
            CitiesPopulations = a.GetCapitalCities();

            // Sort arraylist in ascending order
            CitiesPopulations.sort(Comparator.comparing(City::getCityPopulation));
            System.out.println("POPULATION OF CAPITAL CITIES IN ASCENDING ORDER: ");

            // Output cities and populations
            for(int i = 0; i < CitiesPopulations.size(); i++)
            {
                System.out.println("City " + (i+1) + ": " + CitiesPopulations.get(i).getCityName());
                System.out.println("Population : " + CitiesPopulations.get(i).getCityPopulation() + "\n");
            }

            // Print information on a specific capital city (Use Case 09)
            System.out.println("\n----- CAPITAL CITY REPORT - USE CASE 09 -----");
            City ACity = new City();
            ACity = a.getCapitalCityReport("London");
            System.out.println(ACity.toString());

            // Print population of people not living in cities in a region or country (Use Case 12)
            System.out.println("\n----- PEOPLE NOT LIVING IN CITIES IN REGIONS OR COUNTRIES - USE CASE 12");

            // Get population of a country, both in cities and not in cities, and find percentage of each
            long CountryPop = a.GetPopulation(4, "United Kingdom");
            long CityPop = a.GetAllCityPopulations(2, "United Kingdom");

            percentInCity = ((float) CityPop / (float) CountryPop) * 100;
            percentNotInCity = (((float) CountryPop - (float)CityPop) / (float) CountryPop) * 100;

            System.out.println("People living in the UK : " + String.format("%,d", CountryPop));
            System.out.println("People living in the UK in a city : " + String.format("%,d", CityPop) + " : " + percentInCity + "%");
            System.out.println("People living in the UK not in a city : " + String.format("%,d", CountryPop - CityPop) + " : " + percentNotInCity + "%");

            // Get population of a region, both in cities and not in cities, and find percentage of each
            long RegionPop = a.GetPopulation(3, "British Islands");
            CityPop = a.GetAllCityPopulations(3, "British Islands");

            percentInCity = ((float) CityPop / (float) RegionPop) * 100;
            percentNotInCity = (((float) RegionPop - (float)CityPop) / (float) RegionPop) * 100;

            System.out.println("People living in the British Islands : " + String.format("%,d", RegionPop));
            System.out.println("People living in the British Islands in a city : " + String.format("%,d", CityPop) + " : " + percentInCity + "%");
            System.out.println("People living in the British Islands not in a city : " + String.format("%,d", RegionPop - CityPop) + " : " + percentNotInCity + "%");


            // Terminate connection to the database.
            a.Disconnect();


        }
    }
