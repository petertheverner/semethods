package com.napier.sem;

public class Languages {

    //variables for world languages
    private String Hindi;
    private String English;
    private String Chinese;
    private String Arabic;
    private String Spanish;
    private int Language_Speakers;

    //Setters
    public void setHindi(String Hindi) { this.Hindi = Hindi; }
    public void setEnglish(String English) { this.English = English; }
    public void setChinese(String Chinese) { this.Chinese = Chinese; }
    public void setArabic(String Arabic) { this.Arabic = Arabic; }
    public void setSpanish(String Spanish) { this.Spanish = Spanish; }

    //no of language speakers
    public void setLanguage_Speakers(int Language_Speakers){ this.Language_Speakers = Language_Speakers; }

    //Getters
    public String getHindi() { return this.Hindi; }
    public String getEnglish() { return this.English; }
    public String getChinese() { return this.Chinese; }
    public String getArabic() { return this.Arabic; }
    public String getSpanish() { return this.Spanish; }
    public int getLanguageSpeakers() {return this.Language_Speakers;}

}