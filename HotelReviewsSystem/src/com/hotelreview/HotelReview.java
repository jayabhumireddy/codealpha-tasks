package com.hotelreview;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HotelReview {
	public static void main (String[] args) {
		List<Reviews> reviews = new ArrayList<>(Arrays.asList(
				//data
				
			new Reviews("Cave Kitechen","Vicky",5,"Excellent",LocalDate.of(2024,2,10)),
			new Reviews("Spice&Sparks","Michael",4,"Very Good",LocalDate.of(2024,1,4)),
			new Reviews("RedStar","Daniel",3,"Average",LocalDate.of(2024,5,23)),
			new Reviews("Rainbow","Sangeetha",2,"Not Satisfied",LocalDate.of(2024,3,5))
				));
		
		//Filter reviews with rating >=4
		List<Reviews> filteredReviews = reviews.stream()
				.filter(review -> review.getRating() >=4)
				.collect(Collectors.toList());
		
		System.out.println("Filtered Reviews (Rating >=4):");
		filteredReviews.forEach(System.out::println);
		
		// Sort reviews by date
		List<Reviews> sortedReviews = reviews.stream()
				.sorted(Comparator.comparing(Reviews::getDate))
				.collect(Collectors.toList());
		
		System.out.println("\nsorted Reviews by Date:");
		sortedReviews.forEach(System.out::println);
		
		
	
		
	}
	
}



