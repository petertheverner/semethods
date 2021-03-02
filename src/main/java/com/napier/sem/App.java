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

        // Connect method: Attempts to connect to the database when called. Will
        // attempt to connect several times, and will display a relevant error message
        // each time the connection attempt fails.
        public void Connect() {

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
                    con = DriverManager.getConnection("jdbc:mysql://db:3306/world?useSSL=false", "root", "example");
                    System.out.println("Succesful connection to the database.");
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


        // Disconnect method: Attempts to sever the connection to the database.
        // Returns an error message if the attempt fails.
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
            // Catches any errors that occurr during the query execution and returns an appropiate
            // error message.
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Error retrieving population data for:" + search);
            }
            // Total is returned if no errors are found and the query is successfully executed.
            return total;
        }


        public static void main(String[] args)
        {
            // Initialise the application
            App a = new App();
            // Connect to the database
            a.Connect();

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
            // Terminate connection to the database.
            a.Disconnect();

        }

   }
