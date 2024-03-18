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
                double potentialDistance = distanceMatrix[currentCity.getIndex()-1][city.getIndex()-1];
                bestItem = city.getMostValuableItem(knapsack.getCapacity() - knapsack.getWeight());
                int weight = bestItem == null ? 0 : bestItem.getWeight();

                double potentialSpeed = maxSpeed - (maxSpeed - minSpeed) *
                        ((double) (knapsack.getWeight() + weight) / knapsack.getCapacity());

                double potentialTime = potentialDistance / potentialSpeed;

                if (potentialTime < mitTime) {
                    nextCity = city;
                    mitTime = potentialTime;
                }
            }

            solution.appendSolution(nextCity, mitTime, bestItem);
            currentCity = nextCity;
            unvisitedCities.remove(currentCity);
        }

        return solution;
    }
}
