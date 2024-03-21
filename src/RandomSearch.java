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
    public boolean saveToFile(String fileName, int runNumber) {
        String[] data = new String[6];

        HashMap<String, Number> params = params();
        StringBuilder sb = new StringBuilder();
        sb.append("Random Search - ");

        for (Map.Entry<String, Number> entry :params.entrySet()){
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(" ");
        }

        data[0] = sb.toString().strip();

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
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double sum = 0;
        for (int i=0; i<fitness.size(); i++){
            if(fitness.get(i) < min) min = fitness.get(i);
            if(fitness.get(i) > max) max = fitness.get(i);
            sum += fitness.get(i);

            double mean = sum / (i+1);
            double stD = Math.sqrt(fitness.subList(0,i).stream().mapToDouble(Double::doubleValue).map(
                    x -> Math.pow(x - mean, 2)).sum() / (i+1));

            sb.append(i).append(",").append(min).append(",").append(max).append(",")
                    .append(mean).append(",").append(stD).append("\n");
        }
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
        Double[][] result = new Double[fitness.size()][4];

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double sum = 0;
        for (int i=0; i<fitness.size(); i++){
            if(fitness.get(i) < min) min = fitness.get(i);
            if(fitness.get(i) > max) max = fitness.get(i);
            sum += fitness.get(i);

            double mean = sum / (i+1);
            double stD = Math.sqrt(fitness.subList(0,i).stream().mapToDouble(Double::doubleValue).map(
                    x -> Math.pow(x - mean, 2)).sum() / (i+1));

            result[i][0] = min;
            result[i][1] = max;
            result[i][2] = mean;
            result[i][3] = stD;
        }

        return result;
    }
}
