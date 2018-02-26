package com.studienarbeit.GRAPH;

import java.util.ArrayList;

public class TravelingSalesmanTourManager {
    // Holds our cities
    private static ArrayList destinationCities = new ArrayList<City>();

    // Adds a destination city
    public static void addCity(City city) {
        destinationCities.add(city);
    }
    
    // Get a city
    public static City getCity(int index){
        return (City)destinationCities.get(index);
    }
    
    // Get the number of destination cities
    public static int numberOfCities(){
        return destinationCities.size();
    }
    
    public static ArrayList getDestinationCities(){
        return destinationCities;
    }
}
