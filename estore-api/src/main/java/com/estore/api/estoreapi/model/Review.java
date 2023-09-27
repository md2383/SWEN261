package com.estore.api.estoreapi.model;

public class Review {

    public int productid;
    public int userid;
    public String review;
    public int rating;

    public Review() {}

    /**
     * Create a review with productid, userid, review and rating 
     * @param productid  the productid of the review
     * @param userid  the userid of the review
     * @param review  the review of the review
     * @param rating  the rating of the review
     */
    public Review(int productid, int userid, int ratingoutof5, String review) {
        this.productid = productid;
        this.userid = userid;
        this.review = review;
        this.rating = ratingoutof5;
    }

    public int getProductid() {
        return productid;
    }

    public int getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public int getUserid() {
        return userid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
