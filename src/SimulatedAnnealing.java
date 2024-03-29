import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SimulatedAnnealing implements Optimizer{

    // algorithm understanding based on the following source:
    // https://medium.com/@francis.allanah/travelling-salesman-problem-using-simulated-annealing-f547a71ab3c6
    // no code was copied from the source, only the algorithm understanding was based on it

    private final double coolingRate;
    private final double startTemperature;
    private double temperature;
    private final double terminationTemperature;
    private final double mutationChance;
    private final ArrayList<Double> fitness;

    public SimulatedAnnealing(double coolingRate, double startTemperature,
                              double terminationTemperature, double mutationChance){
        this.coolingRate = coolingRate;
        this.startTemperature = startTemperature;
        this.temperature = startTemperature;
        this.terminationTemperature = terminationTemperature;
        this.mutationChance = mutationChance;
        fitness = new ArrayList<>();
    }

    private boolean acceptanceCriteria(double rivalFitness, double benchmarkFitness){
        double exp = Math.exp((rivalFitness - benchmarkFitness) / temperature);
        double random = Math.random();
        return exp >= random;
    }

    @Override
    public Solution solve() {
        RandomSearch instance = new RandomSearch(1);
        Solution benchmarkSolution = instance.solve();
        Solution rivalSolution = instance.solve();
        Solution bestSolution = benchmarkSolution;

        while(temperature > terminationTemperature){
            double benchmarkFitness = benchmarkSolution.getFitness();
            double rivalFitness = rivalSolution.getFitness();

            fitness.add(rivalFitness);

            if(benchmarkFitness < rivalFitness){
                benchmarkSolution = rivalSolution;
                if (bestSolution.getFitness() < benchmarkFitness) bestSolution = benchmarkSolution;
                rivalSolution = Operators.mutation(benchmarkSolution, mutationChance);
            }
            else if (acceptanceCriteria(rivalFitness, benchmarkFitness)){
                benchmarkSolution = rivalSolution;
                rivalSolution = Operators.mutation(rivalSolution, mutationChance);
            }
            else{
                rivalSolution = instance.solve();
            }

            temperature *= coolingRate;
        }

        return bestSolution;
    }

    @Override
    public HashMap<String, Number> params() {
        HashMap<String, Number> result = new HashMap<>(4);

        result.put("c", coolingRate);
        result.put("sT", startTemperature);
        result.put("tT", terminationTemperature);
        result.put("m", mutationChance);

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
        sb.append("Simulated Annealing ");
        for (Map.Entry<String, Number> entry : params().entrySet()){
            sb.append(entry.getKey()).append("-").append(entry.getValue()).append(" ");
        }

        return sb.toString();
    }

    @Override
    public void reset() {
        temperature = startTemperature;
        fitness.clear();
    }
}
