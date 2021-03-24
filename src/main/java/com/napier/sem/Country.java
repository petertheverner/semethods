package com.napier.sem;

public class Country
{
    public Country() { }

    // Private variables of the country
    private String code;
    private String name;
    private String continent;
    private String region;
    private String capital;
    private int population;

    // Setters
    public void setCode(String Code) { this.code = Code; }
    public void setName(String Name) { this.name = Name; }
    public void setContinent(String Continent) { this.continent = Continent; }
    public void setRegion(String Region) { this.region = Region; }
    public void setCapital(String Capital) { this.capital = Capital; }
    public void setPopulation(int Population) { this.population = Population; }

    // Getters
    public String getName() { return this.name; }
    public int getPopulation() { return this.population; }
    public String getContinent() {return this.continent; }

    // toString output method.
    @Override
    public String toString()
    {
        return "Code : " + this.code +
                "\nCountry Name : " + this.name +
                "\nContinent : " + this.continent +
                "\nRegion : " + this.region +
                "\nCapital City : " + this.capital +
                "\nTotal Population : " + String.format("%,d",this.population);
    }
}