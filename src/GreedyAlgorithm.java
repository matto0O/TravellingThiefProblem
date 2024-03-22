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
    public boolean saveToFile(String fileName, int runNumber) {
        String[] data = new String[6];

        data[0] = "Greedy Algorithm";

        Number[] summary = runSummary(runNumber);
        for (int i=0; i< summary.length; i++){
            data[i+1] = String.valueOf(summary[i]);
        }

        File file = new File(fileName);
        String line = String.join(",", data);

        try (FileWriter fw = new FileWriter(file, true)){
            fw.append(line);
            fw.append('\n');
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String iterationDetails() {

        StringBuilder sb = new StringBuilder();
        sb.append("Iteration,Min,Max,Mean,StD\n");
        sb.append("0,").append(fitness.getFirst()).append(",").append(fitness.getFirst())
                .append(",").append(fitness.getFirst()).append(",0\n");

        return sb.toString();
    }

    @Override
    public String iterationPreview() {
        StringBuilder sb = new StringBuilder();
        sb.append("Iteration,Fitness\n");
        for (int i=0; i<fitness.size(); i++){
            sb.append(i).append(",").append(fitness.get(i)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public Double[][] iterationNumbersPreview() {
        Double[][] result = new Double[1][1];
        result[0][0] = fitness.getFirst();
        return result;
    }
}
