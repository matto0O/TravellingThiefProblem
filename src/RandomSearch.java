import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class RandomSearch implements Optimizer{
    private final int iterations;
    private final ArrayList<Double> fitness;

    public RandomSearch(int iterations){
        this.iterations = iterations;
        fitness = new ArrayList<>();
    }

    @Override
    public Solution solve() {
        Solution bestSolution = null;

        for (int i=0; i<iterations; i++){
            Knapsack knapsack = new Knapsack(Problem.knapsackSize);
            ArrayList<City> unvisitedCities = new ArrayList<>(Arrays.asList(Problem.cities));

            Solution solution = new Solution(knapsack, null);

            while (!unvisitedCities.isEmpty()) {
                City nextCity = unvisitedCities.get(Problem.random.nextInt(unvisitedCities.size()));
                int maxWeight = knapsack.getCapacity() - knapsack.getWeight();
                ArrayList<Item> items = nextCity.getItemsLighterThan(maxWeight);
                int randomIndex = Problem.random.nextInt(items.size() + 1);
                Item selectedItem = items.isEmpty() || randomIndex == items.size() ? null : items.get(randomIndex);

                solution.appendSolution(nextCity, selectedItem);

                unvisitedCities.remove(nextCity);
            }

            solution.calculateFitness();
            fitness.add(solution.getFitness());

            if (bestSolution == null || bestSolution.getFitness() < solution.getFitness()){
                bestSolution = solution;
            }
        }
        if (bestSolution != null) {
            bestSolution.calculateFitness();
        }
        Problem.putRunSummary(runSummary(0));

        return bestSolution;
    }

    @Override
    public HashMap<String, Number> params() {
        HashMap<String, Number> result = new HashMap<>(1);

        result.put("i", iterations);

        return result;
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
                x -> Math.pow(x - (double)result[3], 2)).sum() / fitness.size());

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RandomSearch ");
        for (Map.Entry<String, Number> entry : params().entrySet()){
            sb.append(entry.getKey()).append("-").append(entry.getValue()).append(" ");
        }

        return sb.toString();
    }

    @Override
    public void reset() {
        fitness.clear();
    }
}
