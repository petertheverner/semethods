package com.napier.sem;

public class Region {

    //variables for world regions
    private String RegionName;
    private int Regionpopulation;
    private String RegionCountry;

    //Setters
    public void setRPopulation(int RPopulation) { this.Regionpopulation = RPopulation; }
    public void setRegionName(String RegionName) { this.RegionName = RegionName; }
    public void setRegionCountry(String RegionName) { this.RegionName = RegionName; }


    //Getters
    public int    getRegionpopulation() { return this.Regionpopulation; }
    public String getRegionName() { return this.RegionName; }
    public String getRegionCountry() { return this.RegionCountry; }

    // toString output method.
    @Override
    public String toString()
    {
        return  "\nRegion Name : " + this.RegionName +
                "\nTotal Population : " + String.format("%,d",this.Regionpopulation)+
                "\nCountry of Region: " + this.RegionCountry;
    }
}