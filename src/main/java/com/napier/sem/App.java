package com.napier.sem;

import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.sql.*;

    public class App
    {
        private Connection con = null;

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

        public static void main(String[] args)
        {
            App a = new App();
            a.Connect();
        }
   }
