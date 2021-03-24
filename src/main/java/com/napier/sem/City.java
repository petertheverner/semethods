package com.napier.sem;

public class City {

    //variables for City
    private String Cityname;
    private int Citypopulation;
    private String Citycountry;
    private String Citydistrict;


    //Setters
    public void setCitypopulation(int CityPopulation) { this.Citypopulation =  CityPopulation; }
    public void setCityName(String CityName) { this.Cityname = CityName; }
    public void setCityCountry(String cityCountry) {this.Citycountry = cityCountry; }
    public void setCitydistrict(String citydistrict) {this.Citydistrict = citydistrict;}
    //Getters
    public int getCityPopulation() { return this.Citypopulation; }
    public String getCityName() { return this.Cityname; }

    // toString output method.
    @Override
    public String toString()
    {
        return "City Name : " + this.Cityname +
                "\nCity Population : " + String.format("%,d",this.Citypopulation) +
                "\nCity District : " + this.Citydistrict +
                "\nCity Country : " + this.Citycountry;

    }
}
