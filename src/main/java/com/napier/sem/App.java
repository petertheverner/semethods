/* Authors:
           Liam Blair - 40437578
           Peter Verner - 40488882

   Last modified: 24/02/2020
*/
package com.napier.sem;
// Imports all SQL methods.
import org.graalvm.compiler.lir.LIRInstruction;

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
                    Thread.sleep(30000);
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

            if(searchType  > 1 && (search == "" || search == null))
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
            int CityNameSearch = 0;
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
                    tempCity.setCityCountry(rset.getString("CountryCode"));

                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Error retrieving the report for the city " + city);
                return null;
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

        public City getCapitalCityReport(String city)
        {
            // Init variables to populate city object
            int cityID = -1;
            int countryCode = -1;
            City TempCity = null;

            // Setup initial query to get simple city data
            String query = "SELECT Name, District, Population, CountryCode "
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
                 countryCode = rset.getInt("CountryCode");
                }

                else
                {
                    System.out.println("Error! City not found!");
                }
            }

            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }

            query = "SELECT Capital "
                    +"FROM country "
                    +"WHERE Capital='" + cityID + "'";

            try
            {
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery(query);
                if(rset.next())
                {
                    if(cityID == rset.getInt(("Capital")))
                    {
                        return TempCity;
                    }
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
            return TempCity;
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

            // Print populations of regions (Use Case 04)
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


            // Print report of a specified country (Use Case 08)
            System.out.println("\n----- CITY REPORT - Use Case 08 -----");
            System.out.println("\n City report of the city 'Edinburgh' : ");
            // Calls the GetCountryReport method which returns a country object. Then it calls
            // the toString method of the Country class which returns a text output.
            System.out.println(a.GetCityReport("Edinburgh").toString());


            // Print N most populated countries in the world (Use Case 02).
            System.out.println("\n----- COUNTRY POPULATION BY NUMBER - USE CASE 2 -----");
            System.out.println("\nTop 10 most populated countries : ");
            // Init array for output
            ArrayList <Country> Countries = new ArrayList<Country>();
            // Retrieve array
            Countries = a.GetCountryPopulations(10);
            // Output
            for(int i = 0; i < 10; i++)
            { System.out.println("Country " +  " : " + Countries.get(i).getName());
                System.out.println("Population : " + String.format("%,d", Countries.get(i).getPopulation()));
            }


            // Print N most populated city's in the world (Use Case 14).
            System.out.println("\n----- CITY POPULATION BY NUMBER - USE CASE 14 -----");
            System.out.println("\nTop 10 most populated City's : ");
            // Init array for output
            ArrayList <City> Citys = new ArrayList<City>();
            // Retrieve array
            Citys = a.GetCityPopulations(10);
            // Output
            System.out.println("TOP 10 MOST POPULATED CITY'S : ");
            for(int i = 0; i < 10; i++)
            { System.out.println("City Name" +  " : " + Citys.get(i).getCityName());
                System.out.println(" City Population : " + String.format("%,d", Citys.get(i).getCityPopulation()));
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
            ACity = a.getCapitalCityReport("Edinburgh");

            // Terminate connection to the database.
            a.Disconnect();


        }
    }
