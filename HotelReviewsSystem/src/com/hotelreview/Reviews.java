package com.hotelreview;

import java.time.LocalDate;

public class Reviews {
	private String hotelName;
	private String userName;
	private int rating;
	private String reviewText;
	private LocalDate date;
	
	public Reviews(String hotelName, String userName, int rating, String reviewText, LocalDate date) {
	
		this.hotelName = hotelName;
		this.userName = userName;
		this.rating = rating;
		this.reviewText = reviewText;
		this.date = date;
	}
	
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getReviewText() {
		return reviewText;
	}
	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return"Review{" + "hotelName=" + hotelName +", userName=" + userName +", rating=" + rating + ", reviewText=" +
                reviewText +", date='" + date + '\'' + '}';
	}

}


