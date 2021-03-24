/* Authors:
           Liam Blair - 40437578
           Peter Verner - 40488882

   Last modified: 24/02/2020
*/
package com.napier.sem;
// Imports all SQL methods.
import java.sql.*;
import java.util.ArrayList;

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
                    System.out.println("Failed to connect to database on attempt number" + i);
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
            ArrayList<City> Citys = new ArrayList<City>();
            String query = "SELECT CityPopulation, CityName "
                    +"FROM city "
                    +"ORDER BY CityPopulation DESC";

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
                    tempCity.setCitypopulation(rset.getInt("City Population"));
                    tempCity.setCityName(rset.getString("City Name"));
                    Citys.add(tempCity);
                    i++;
                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Error retrieving population data for top populated city's.");
            }

            return Citys;
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
            String query = "SELECT Cityname, Citypopulation, Citydistrict, Citycountry "
                    +"FROM city "
                    +"WHERE Cityname = '" + city + "'";
            try
            {
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery(query);

                if(rset.next())
                {
                    CityNameSearch = rset.getInt("City's Region");
                    tempCity.setCityName(rset.getString("City's Name"));
                    tempCity.setCitypopulation(rset.getInt("City's Population"));
                    tempCity.setCitydistrict(rset.getString("City's District"));
                    tempCity.setCityCountry(rset.getString("Country"));

                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Error retrieving the report for the city " + city);
                return null;
            }


            try
            {
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery("SELECT Name FROM city WHERE id = '" + CityNameSearch + "'");
                if(rset.next())
                {
                    tempCity.setCityName(rset.getString("Name"));
                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Error retrieving the City Name from the list " + city);
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
            };

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
            System.out.println("TOP 10 MOST POPULATED COUNTRIES : ");
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

            // Terminate connection to the database.
            a.Disconnect();



        }
    }
