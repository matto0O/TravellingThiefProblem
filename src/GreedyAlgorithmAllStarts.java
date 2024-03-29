import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class GreedyAlgorithmAllStarts implements Optimizer{
    private final ArrayList<Double> fitness;

    public GreedyAlgorithmAllStarts(){
        fitness = new ArrayList<>();
    }

    @Override
    public Solution solve() {
        Solution bestSolution = null;
        for (int i=0; i<Problem.cities.length; i++){
            ArrayList<City> unvisitedCities = new ArrayList<>(Arrays.asList(Problem.cities));
            Knapsack knapsack = new Knapsack(Problem.knapsackSize);

            City currentCity = unvisitedCities.remove(i);
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

            if(bestSolution == null || solution.getFitness() > bestSolution.getFitness())
                bestSolution = solution;
        }
        Problem.putRunSummary(runSummary(0));
        return bestSolution;
    }

    @Override
    public HashMap<String, Number> params() {
        return null;
    }

    @Override
    public Number[] runSummary(int runNumber) {
        Number[] result = new Number[5];

        result[0] = runNumber;
        result[1] = Collections.min(fitness);
        result[2] = Collections.max(fitness);
        // average
        result[3] = fitness.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        // standard deviation
        result[4] = Math.sqrt(fitness.stream().mapToDouble(Double::doubleValue).map(
                x -> Math.pow(x - fitness.stream().mapToDouble(Double::doubleValue).average().orElse(0), 2)).sum()
                / fitness.size());

        return result;
    }

    @Override
    public String toString() {
        return "GreedyAlgorithmAllStarts";
    }

    @Override
    public void reset() {
        fitness.clear();
    }
}
