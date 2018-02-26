package com.studienarbeit.PSO;

import java.util.ArrayList;

import com.studienarbeit.GRAPH.City;
import com.studienarbeit.GRAPH.TravelingSalesmanTourManager;
import com.studienarbeit.GRAPH.TravelingSalesmanTour;

public class test {
	public static void main(String[] args) {
        City city = new City(60, 200);
        TravelingSalesmanTourManager.addCity(city);
        City city2 = new City(180, 200);
        TravelingSalesmanTourManager.addCity(city2);
        City city3 = new City(80, 180);
        TravelingSalesmanTourManager.addCity(city3);
        City city4 = new City(140, 180);
        TravelingSalesmanTourManager.addCity(city4);
        City city5 = new City(20, 160);
        TravelingSalesmanTourManager.addCity(city5);
        City city6 = new City(100, 160);
        TravelingSalesmanTourManager.addCity(city6);
        City city7 = new City(200, 160);
        TravelingSalesmanTourManager.addCity(city7);
        City city8 = new City(140, 140);
        TravelingSalesmanTourManager.addCity(city8);
        City city9 = new City(40, 120);
        TravelingSalesmanTourManager.addCity(city9);
        City city10 = new City(100, 120);
        TravelingSalesmanTourManager.addCity(city10);
        City city11 = new City(180, 100);
        TravelingSalesmanTourManager.addCity(city11);
        City city12 = new City(60, 80);
        TravelingSalesmanTourManager.addCity(city12);
        City city13 = new City(120, 80);
        TravelingSalesmanTourManager.addCity(city13);
        City city14 = new City(180, 60);
        TravelingSalesmanTourManager.addCity(city14);
        City city15 = new City(20, 40);
        TravelingSalesmanTourManager.addCity(city15);
        City city16 = new City(60, 40);
        TravelingSalesmanTourManager.addCity(city16);
        City city17 = new City(100, 40);
        TravelingSalesmanTourManager.addCity(city17);
        City city18 = new City(30, 20);
        TravelingSalesmanTourManager.addCity(city18);
        City city19 = new City(60, 20);
        TravelingSalesmanTourManager.addCity(city19);
        City city20 = new City(160, 20);
        TravelingSalesmanTourManager.addCity(city20);
        
        double distanceBetweenCity20City19 = city20.distanceTo(city19);
        System.out.println(distanceBetweenCity20City19);
        
        int numberOfCities = TravelingSalesmanTourManager.numberOfCities();
        System.out.println(numberOfCities);
        
        TravelingSalesmanTour  tourtest = new TravelingSalesmanTour(TravelingSalesmanTourManager.getDestinationCities());
        int numberOfCitiesInTour = tourtest.tourSize();
        System.out.println(numberOfCitiesInTour);
        
        int distanceOfTour = tourtest.getDistance();
        System.out.println(distanceOfTour);
        
	}

}
