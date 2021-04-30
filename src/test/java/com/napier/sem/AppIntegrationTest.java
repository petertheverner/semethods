package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    // Test that the correct worldwide population is retrieved.
    @Test
    @DisplayName("GetPopulation World")
    void testGetPopulationWorld()
    {
        long worldPop = a.GetPopulation(1, "");
        assertEquals(6078749450L, worldPop, "Error! Incorrect worldwide population!");
    }

    // Test that the correct population of a continent is being retrieved.
    @Test
    void TestGetPopulationContinent()
    {
        long continentPop = a.GetPopulation(2, "South America");
        assertEquals(345780000, continentPop, "Error! Incorrect population for the continent South America");
    }

    // Test that the correct population of a region is being retrieved.
    @Test
    void TestGetPopulationRegion()
    {
        long regionPop = a.GetPopulation(3, "British Islands");
        assertEquals(63398500, regionPop, "Error! Incorrect population for the region British Islands");
    }

    // Test that the correct population of a country is being retrieved
    @Test
    void TestGetPopulationCountry()
    {
        long countryPop = a.GetPopulation(4, "United Kingdom");
        assertEquals(59623400, countryPop, "Error! Incorrect population for country United Kingdom");
    }

    // Test that the correct population of a district is being retrieved
    @Test
    void TestGetPopulationDistrict()
    {
        long districtPop = a.GetPopulation(5, "Scotland");
        assertEquals(1429620, districtPop, "Error! Incorrect population for district Scotland");
    }

    // Test that the correct population of a city is being retrieved
    @Test
    void TestGetPopulationCity()
    {
        long cityPop = a.GetPopulation(6, "Seto");
        assertEquals(130470, cityPop, "Error! Incorrect population for the city Seto");
    }


    // Test for the method : GetCountryReport(String Country)

    // Test that a country report is built for the country spain. Test population
    // country name, and the country's continent
    @Test
    void TestGetCountryReport()
    {
        Country count = a.GetCountryReport("Spain");
        assertEquals(39441700, count.getPopulation(), "Error! Incorrect population for country Spain");
    }


    // Tests for the method : GetCountryPopulations(int count)

    @Test
    void TestGetCountryPopulations()
    {
        ArrayList <Country> Countries;
        Countries = a.GetCountryPopulations(5);
        assertEquals(212107000, Countries.get(3).getPopulation(), "Error! Incorrect population for the 3rd most populated country");
    }


    // Tests for the method GetCityPopulations(int count)

    // General test
    @Test
    void TestGetCityPopulations()
    {
        ArrayList<City> Cities = a.GetCityPopulations(2);
        assertEquals(9981619, Cities.get(1).getCityPopulation(), "Error! Invalid population for the 2nd most populated city");

    }

    // Test passing in an invalid value (in this case, 0).

    @Test
    void TestGetCityPopulationsNullCount()
    {
        ArrayList<City> Cities = a.GetCityPopulations(0);
        assertEquals(null, Cities);
    }


    // Tests for the method GetCityReport(string city)

    // General test
    @Test
    void TestGetCityReport()
    {
        City aCity = a.GetCityReport("Edinburgh");
        assertEquals("Scotland", aCity.getCityDistrict(), "Error! Incorrect city information provided");
    }



    // Tests for the method : GetCapitalCities()
    @Test
    void TestGetCapitalCities()
    {
        ArrayList<City> Cities;
        Cities = a.GetCapitalCities();
        Cities.sort(Comparator.comparing(City::getCityPopulation));

        assertEquals("Seoul", Cities.get(Cities.size()-1).getCityName(), "Error! Wrong city selected for having the largest population");
    }


    // Tests for the method: GetCapitalCityReport(String city)

    // Test inputting a city that is not a capital city
    @Test
    void TestGetCapitalCityReportNonCapital()
    {
        City tempCity = a.getCapitalCityReport("Breda");
        assertEquals(null, tempCity, "Error! Non - capital city was interpreted as a capital");
    }

    // Test to ensure that the method returns a correct value.
    @Test
    void TestGetCapitalCityReport()
    {
        City tempCity = a.getCapitalCityReport("London");
        assertEquals("England", tempCity.getCityDistrict(), "Error! Incorrect district retrieved for London");
    }


    // Tests for the method GetAllCityPopulations(int searchType, String area)

    // Test that an error is returned if an invalid location is provided
    @Test
    void TestCityPopulationsInvalidArea()
    {
        long tempNumber = a.GetAllCityPopulations(2, "asdf");
        assertEquals(-1, tempNumber, "Error! Invalid location was accepted");
    }

    // Test by giving a valid location but the wrong location type (in this case, United Kingdom being interpreted as a continent)
    @Test
    void TestCityPopulationsWrongArea()
    {
        long tempNumber = a.GetAllCityPopulations(1, "United Kingdom");
        assertEquals(-1, tempNumber, "Error! UK Country was interpreted as a continent");
    }

    // Normal test to ensure that the output is correct.
    @Test
    void TestCityPopulations()
    {
        long tempNumber = a.GetAllCityPopulations(2, "United Kingdom");
        assertEquals(22436672, tempNumber, "Error! Wrong total city populations of the country!");
    }


    // Tests for the method Disconnect()

    // Test disconnect method with a successful connection
    @Test void TestDisconnect()
    {
        a.Disconnect();
        // Re - establish connection for other tests
        a.Connect("localhost:33060");
    }

    // Test disconnect method without a connection
    @Test void TestDisconnectNullCon()
    {
        // Disonnecting currently running connection
        a.Disconnect();
        // Attempt to disconnect again.
        a.Disconnect();
        // Re - establish connection for other tests.
        a.Connect("localhost:33060");
    }
}
