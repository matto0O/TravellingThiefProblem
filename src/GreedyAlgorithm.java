import java.util.ArrayList;
import java.util.Arrays;

public class GreedyAlgorithm implements Optimizer{
    @Override
    public Solution solve(City[] cities, int knapsackSize, double minSpeed,
                          double maxSpeed, double[][] distanceMatrix, int iterations) {
        ArrayList<City> unvisitedCities = new ArrayList<>(Arrays.asList(cities));
        Knapsack knapsack = new Knapsack(knapsackSize);
        City currentCity = unvisitedCities.removeFirst();
        Solution solution = new Solution(knapsack, currentCity);

        Item bestItem = null;

        while (!unvisitedCities.isEmpty()) {
            City nextCity = unvisitedCities.getFirst();
            double mitTime = Double.MAX_VALUE;
            for (City city : unvisitedCities) {
                bestItem = city.getMostValuableItem(knapsack.getCapacity() - knapsack.getWeight());

                double potentialTime = Problem.timeBetween(currentCity, city, knapsack);

                if (potentialTime < mitTime) {
                    nextCity = city;
                    mitTime = potentialTime;
                }
            }

            solution.appendSolution(nextCity, bestItem);
            currentCity = nextCity;
            unvisitedCities.remove(currentCity);
        }
        solution.calculateFitness();
        return solution;
    }
}
