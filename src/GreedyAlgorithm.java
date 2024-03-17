import java.util.ArrayList;
import java.util.Arrays;

public class GreedyAlgorithm implements Optimizer{
    @Override
    public void solve(City[] cities, Knapsack knapsack) {
        ArrayList<City> unvisitedCities = new ArrayList<>(Arrays.asList(cities));

        City currentCity = unvisitedCities.remove(0);
        cities[0] = currentCity;

        while (!unvisitedCities.isEmpty()) {
            City nextCity = unvisitedCities.get(0);
            double minDistance = currentCity.distanceTo(nextCity);
            for (City city : unvisitedCities) {
                double potentialDistance = currentCity.distanceTo(city);
                if (potentialDistance < minDistance) {
                    nextCity = city;
                    minDistance = potentialDistance;
                }
            }
            currentCity = nextCity;
            cities[cities.length - unvisitedCities.size()] = currentCity;
            unvisitedCities.remove(currentCity);
        }

        for (City city : cities) {
            System.out.println(city);
        }
    }
}
