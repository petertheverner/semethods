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

        // Method that retrieves the world population, stores it in a long, and returns it.
        public long GetWorldPopulation()
        {
             // A long is used to store the world population. Both an int and an unsigned int are too small to
             // store the number.
             long total = 0;
            try
            {
                // Create statement that selects the population field from the country table.
                Statement stmt = con.createStatement();
                String query = "SELECT Population FROM country";
                ResultSet rset = stmt.executeQuery(query);

                // Runs through all retrieved rows.
                while(rset.next())
                {
                    // Add the population from the retrieved row to the total.
                    total += rset.getInt("Population");
                }
            }
            // Catches and throws an error if an error is encountered.
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Failed to retrieve world population");
                return -1;
            }
            return total;
        }

        // Method that retrieves the population of a continent.
        public long GetContinentPopulation()
        {
            // long used to store larger numerical values, int datatype is too small.
            long total = 0;
            // Create statement to search database for the population field in the
            // country database where the country's continent is equal to "Europe".
            try
            {
                Statement stmt = con.createStatement();
                String query = "SELECT Population "
                        +"FROM country "
                        +"WHERE Continent = 'Europe'";
                ResultSet rset = stmt.executeQuery(query);

                // Loop through all retrieved populations, add to total.
                while(rset.next())
                {
                    total += rset.getInt("Population");
                }
            }
            // Catch errors and return -1 and error response to indicate the method failed to
            // run correctly.
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Failed to retrieve continent population.");
                return -1;
            }
            return total;
        }


        public static void main(String[] args)
        {
            // Initialise the application
            App a = new App();
            // Connect to the database
            a.Connect();

            // Print populations of regions ( Use Case 04)
            System.out.println("----- POPULATIONS - Use Case 04 -----");

            System.out.println("\nPopulation of the world");
            System.out.println(">> " + String.format("%,d",a.GetWorldPopulation()) + " <<");
            System.out.println("Population of a continent (Europe)");
            System.out.println(">> " + String.format("%,d", a.GetContinentPopulation()) + " <<");

            // Terminate connection to the database.
            a.Disconnect();

        }

   }
