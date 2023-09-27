package com.estore.api.estoreapi.model;

public class Address {
    private String city;
    private String street;
    private String houseNumber;
    private int zip;
    private String state;

    public Address() {}

    /**
     * Create an address that has multiple components 
     * @param city  city name 
     * @param street  street name  
     * @param state  state name 
     * @param houseNumber  house number 
     * @param zip  zipcode 
     * 
     */
    public Address(String city, String street, String state, String houseNumber, int zip) {
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.zip = zip;
        this.state = state;
    }

    /**
     * get city name 
     * @return string city 
     */
    public String getCity() {
        return city;
    }

    /**
     * get street 
     * @return string street 
     */
    public String getStreet() {
        return this.street;
    }
    
    /**
     * get state name 
     * @return string state 
     */
    public String getState() {
        return this.state;
    }

    /**
     * set state 
     * @param string state 
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * set street 
     * @param string street 
     */
    public void setStreet(String street) { 
        this.street = street; 
    }

    /**
     * get house number 
     * @return string houseNumber 
     */
    public String getHouseNumber() { 
        return this.houseNumber; 
    }

    /**
     * set house number 
     * @param string houseNumber 
     */
    public void setHouseNumber(String houseNumber) { 
        this.houseNumber = houseNumber; 
    }

    /**
     * get zip code  
     * @return int zip  
     */
    public int getZip() { 
        return this.zip; 
    }

    /**
     * set zip code  
     * @param int zip 
     */
    public void setZip(int zip) { 
        this.zip = zip; 
    }

    /**
     * set city name 
     * @return string city  
     */
    public void setCity(String city) {
        this.city = city;
    }

}
