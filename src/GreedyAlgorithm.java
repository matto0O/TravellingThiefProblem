import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class GreedyAlgorithm implements Optimizer{
    private final ArrayList<Double> fitness;

    public GreedyAlgorithm(){
        fitness = new ArrayList<>();
    }

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
        fitness.add(solution.getFitness());
        Problem.putRunSummary(runSummary(0));
        return solution;
    }

    @Override
    public HashMap<String, Number> params() {
        return null;
    }

    @Override
    public Number[] runSummary(int runNumber) {
        Number[] result = new Number[5];

        double item = fitness.getFirst();

        result[0] = runNumber;
        result[1] = item;
        result[2] = item;
        // average
        result[3] = item;
        // standard deviation
        result[4] = 0.0;

        return result;
    }

    @Override
    public String toString() {
        return "GreedyAlgorithm";
    }

    @Override
    public void reset() {
        fitness.clear();
    }
}
