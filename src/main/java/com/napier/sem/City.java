package com.napier.sem;

public class City {

    //variables for City
    private String Cityname;
    private int Citypopulation;


    //Setters
    public void setCitypopulation(int CityPopulation) { this.Citypopulation =  CityPopulation; }
    public void setCityName(String CityName) { this.Cityname = CityName; }

    //Getters
    public int getCityPopulation() { return this.Citypopulation; }
    public String getCityName() { return this.Cityname; }
}
