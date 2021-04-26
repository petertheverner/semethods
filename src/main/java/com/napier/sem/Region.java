package com.napier.sem;

public class Region {

    //variables for world regions
    private String RegionName;
    private int Regionpopulation;

    //Setters
    public void setRPopulation(int RPopulation) { this.Regionpopulation = RPopulation; }
    public void setRegionName(String RegionName) { this.RegionName = RegionName; }

    //Getters
    public int getRegionpopulation() { return this.Regionpopulation; }
    public String getRegionName() { return this.RegionName; }

    // toString output method.
    @Override
    public String toString()
    {
        return  "\nCountry Name : " + this.RegionName +
                "\nTotal Population : " + String.format("%,d",this.Regionpopulation);
    }
}
