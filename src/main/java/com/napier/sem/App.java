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
                    con = DriverManager.getConnection("jdbc:mysql://DB:3306/world?useSSL=false", "root", "example");
                    System.out.println("Succesful connection to the database.");
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
                    System.out.println("Successfully terminated connection to the database");
                }
                catch (Exception e)
                {
                    System.out.println("Error terminating connection to the database.");
                }
            }
        }

        // TEST METHOD: Used to ensure that the database connection works and
        // can retrieve data from the database.
        public ArrayList<Country> GetAllCountries()
        {
            try {
                Statement stmt = con.createStatement();
                String query = "SELECT Code, Name "
                        + "FROM country";
                ResultSet rset = stmt.executeQuery(query);
                ArrayList<Country> Countries = new ArrayList<>();

                while(rset.next())
                {
                    Country tempCount = new Country();
                    tempCount.code = rset.getString("Code");
                    tempCount.name = rset.getString("Name");
                    Countries.add(tempCount);
                }
                return Countries;
            }

            catch (Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Failed to retrieve country names");
                return null;
            }
        }

        public void Display(ArrayList<Country> Countries)
        {
            for (Country count : Countries)
            {
                System.out.println("Name : " + count.name);
                System.out.println("Code : " + count.code);
                System.out.println("--------------------------");
            }
            System.out.println("Number of countries : " + Countries.size());
        }


        public static void main(String[] args)
        {
            App a = new App();
            a.Connect();
            ArrayList<Country> Countries = a.GetAllCountries();
            a.Display(Countries);
            a.Disconnect();

        }

   }
