package com.estore.api.estoreapi.model;

public class Payment {
    private String cardHolder;
    private String cardNumber;
    private int cvv;
    private String expDate;
    
    public Payment() {}
    /**
     * Holds payment Info 
     * @param cardHolder string name of card holder 
     * @param cardNumber string number of the card 
     * @param expDate string of the date 
     * @cvv int of cvv 
     */
    public Payment(String cardHolder, String cardNumber, String expDate, int cvv) {
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expDate = expDate;
    }

    //get string name of card holder 
    public String getCardHolder() {
        return this.cardHolder;
    }

    //set string name of card holder 
    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    //get string card number 
    public String getCardNumber() {
        return this.cardNumber;
    }

    //set string card number 
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    //get int cvv 
    public int getCvv() {
        return this.cvv;
    }

    //set int cvv 
    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    //get string exp date 
    public String getExpDate() {
        return this.expDate;
    }

    //set string exp date 
    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }
}
