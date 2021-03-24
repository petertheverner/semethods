package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest {
    // init app
    static App a;

    // Setup app and connect to database before running tests.
    @BeforeAll
    static void init()
    {
        a = new App();
        a.Connect("localhost:33060");
    }

    // Tests for GetPopulation(int searchType, string search)

    // Test that the correct population of a continent is being retrieved.
    @Test
    void TestGetPopulationContinent()
    {
        long continentPop = a.GetPopulation(2, "South America");
        assertEquals(345780000, continentPop);
    }

    // Test that the correct population of a city is being retrieved
    @Test
    void TestGetPopulationCity()
    {
        long cityPop = a.GetPopulation(6, "Seto");
        assertEquals(130470, cityPop);
    }

    // Test that the correct population of a country is being retrieved
    @Test
    void TestGetPopulationCountry()
    {
        long countryPop = a.GetPopulation(4, "United Kingdom");
        assertEquals(59623400, countryPop);
    }


    // Test for the method : GetCountryReport(String Country)

    // Test that a country report is built for the country spain. Test population
    // country name, and the country's continent
    @Test
    void TestGetCountryReport()
    {
        Country count = a.GetCountryReport("Spain");
        assertEquals(39441700, count.getPopulation());
        assertEquals("Spain", count.getName());
        assertEquals("Europe", count.getContinent());
    }

    // Tests for the method : GetCountryPopulations(int count)

    @Test
    void TestGetCountryPopulations()
    {
        ArrayList <Country> Countries = new ArrayList<Country>();
        Countries = a.GetCountryPopulations(5);
        for(int i = 0; i < Countries.size(); i++)
        {   System.out.println("Country " +  " : " + Countries.get(i).getName());
            System.out.println("Population : " + String.format("%,d", Countries.get(i).getPopulation()));
        }
        assertEquals(1277558000, Countries.get(0).getPopulation());
        assertEquals(1013662000, Countries.get(1).getPopulation());
        assertEquals(278357000, Countries.get(2).getPopulation());
        assertEquals(212107000, Countries.get(3).getPopulation());
        assertEquals(170115000, Countries.get(4).getPopulation());


    }

}
