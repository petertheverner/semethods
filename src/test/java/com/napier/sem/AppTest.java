package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    // Initialise the app
    static App a;

    // Assign app object, done before all other unit tests.
    @BeforeAll
    static void init() { a = new App(); }

    /**
    * Tests for the method : GetPopulation(int searchType, string search).
    *
    * countType outside the range 1 - 7.
    * If countType is outside the range, -1 is returned.
     **/
    @Test
    void TestGetPopulationRange()
    {
        assertEquals(-1, a.GetPopulation(8, "United Kingdom"), "Error! Invalid value for searchType passed");
    }

    // Test if the method can handle an empty string with a search type of > 1.
    @Test
    void TestGetPopulationEmpty()
    {
        assertEquals(-1, a.GetPopulation(4, ""), "Error! Empty string passed for search target");
    }

    // Test if the method can handle null strings with a negative search range.
    @Test
    void TestGetPopulationNull()
    {
        assertEquals(-1, a.GetPopulation(-3, null), "Error! Negative value for searchType passed");
    }


    //Tests for the method : GetCountryReport(String Country)

    // Test Country Report with an null input (The method will return an empty country
    // object with the population set to -1 to denote an error.
    @Test
    void TestGetCountryReportNull()
    {
        Country testCountry = a.GetCountryReport(null);
        assertEquals(-1, testCountry.getPopulation(), "Error! Null value passed for a country input");
    }

    // Test country report with an empty input.
    @Test
    void TestGetCountryReportEmpty()
    {
        Country testCountry = a.GetCountryReport("");
        assertEquals(-1, testCountry.getPopulation(), "Error! Empty string country input passed");
    }

    // Tests for the method GetCountryPopulations(int count)

    // Test country populations generator with a count of 0. Because this method returns
    // an array list, it will check for an empty arraylist, a size of 0.
    @Test
    void TestGetCountryPopulationsZero()
    {
        int arraySize = a.GetCountryPopulations(0).size();
        assertEquals(0, arraySize, "Error! 0 country count passed");
    }

    // Test country populations generator with a negative count
    @Test
    void TestGetCountryPopulationsNegative()
    {
        int arraySize = a.GetCountryPopulations(-20).size();
        assertEquals(0, arraySize, "Error! Negative number of countries passed");
    }

    // Tests for the method getCapitalCityReport(String city)

    // Test by passing in an empty city input string
    @Test
    void TestGetCapitalCityEmptyString()
    {
        City tempCity = a.getCapitalCityReport("");
        assertEquals(null, tempCity, "Error! Empty string passed for a city input");
    }

    // Test by passing in a null input
    @Test
    void TestGetCapitalCityNull()
    {
        City tempCity = a.getCapitalCityReport(null);
        assertEquals(null, tempCity, "Error! Null value passed for a city input");
    }

    // No unit tests can be done for the method GetCapitalCities as no matter what, the user cannot
    // affect the output; No inputs are given.


    // Tests for the method GetAllCityPopulations(int searchType, String area)

    // Test passing in an invalid search type value ( < 1 or > 3)
    @Test
    void TestCityPopulationsInvalidType()
    {
        long tempNumber = a.GetAllCityPopulations(10, "United Kingdom");
        assertEquals(-1, tempNumber, "Error! Invalid search type passed to method");
    }

}
