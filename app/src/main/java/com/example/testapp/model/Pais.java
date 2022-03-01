package com.example.testapp.model;

public class Pais {
    String name;
    String capital;
    String abbrev;
    String int_name;

    public Pais(String name, String capital, String abbrev, String int_name) {
        this.name = name;
        this.capital = capital;
        this.abbrev = abbrev;
        this.int_name = int_name;
    }

    @Override
    public String toString() {
        return  name;
    }

    public Pais(){
    }

    public String getName() {
        return name;
    }

    public String getCapital() {
        return capital;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public String getInt_name() {
        return int_name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    public void setInt_name(String int_name) {
        this.int_name = int_name;
    }
}
