package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.util.ArrayList;
import java.util.Comparator;

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


    // Tests for the method : GetCapitalCities()
    @Test
    void TestGetCapitalCities()
    {
        ArrayList<City> Cities = new ArrayList<City>();
        Cities = a.GetCapitalCities();
        Cities.sort(Comparator.comparing(City::getCityPopulation));

        assertEquals("Adamstown", Cities.get(0).getCityName());
        assertEquals(42, Cities.get(0).getCityPopulation());

        assertEquals("Seoul", Cities.get(Cities.size()-1).getCityName());
        assertEquals(9981619, Cities.get(Cities.size()-1).getCityPopulation());
    }


    // Tests for the method: GetCapitalCityReport(String city)

    // Test inputting a city that is not a capital city
    @Test
    void TestGetCapitalCityReportNonCapital()
    {
        City tempCity = a.getCapitalCityReport("Breda");
        assertEquals(null, tempCity);
    }

    // Test to ensure that the method returns a correct value.
    @Test
    void TestGetCapitalCityReport()
    {
        City tempCity = a.getCapitalCityReport("London");
        City Edinburgh = new City();

        assertEquals("London", tempCity.getCityName());
        assertEquals(7285000, tempCity.getCityPopulation());
        assertEquals("England", tempCity.getCityDistrict());
        assertEquals("United Kingdom", tempCity.getCityCountry());
    }


    // Tests for the method GetAllCityPopulations(int searchType, String area)

    // Test that an error is returned if an invalid location is provided
    @Test
    void TestCityPopulationsInvalidArea()
    {
        long tempNumber = a.GetAllCityPopulations(2, "asdf");
        assertEquals(-1, tempNumber);
    }

    // Test by giving a valid location but the wrong location type (in this case, United Kingdom being interpreted as a continent)
    @Test
    void TestCityPopulationsWrongArea()
    {
        long tempNumber = a.GetAllCityPopulations(1, "United Kingdom");
        assertEquals(-1, tempNumber);
    }

    // Normal test to ensure that the output is correct.
    @Test
    void TestCityPopulations()
    {
        long tempNumber = a.GetAllCityPopulations(2, "United Kingdom");
        assertEquals(22436672, tempNumber);
    }
}
