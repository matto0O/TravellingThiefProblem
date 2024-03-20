import java.util.ArrayList;
import java.util.Arrays;

public class GreedyAlgorithm implements Optimizer{
    @Override
    public Solution solve() {
        ArrayList<City> unvisitedCities = new ArrayList<>(Arrays.asList(Problem.cities));
        Knapsack knapsack = new Knapsack(Problem.knapsackSize);

        City currentCity = unvisitedCities.removeFirst();
        Item bestItem = null;
        Solution solution = new Solution(knapsack, currentCity);

        while (!unvisitedCities.isEmpty()) {
            City nextCity = null;
            double mitTime = Double.MAX_VALUE;
            for (City city : unvisitedCities) {
                Knapsack tempKnapsack = knapsack.copy();
                bestItem = city.getMostValuableItem(tempKnapsack.getCapacity() - tempKnapsack.getWeight());

                if (bestItem != null)
                    tempKnapsack.putItem(bestItem);

                double potentialTime = Problem.timeBetween(currentCity, city, tempKnapsack);

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
