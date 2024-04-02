import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        String FILENAME = "resultsOperators2.csv";

        Optimizer[] greedyStrategies = {
//                new GreedyAlgorithmAllStarts(),
        };

        Optimizer[] otherStrategies = {
            new EvolutionaryAlgorithm(1000, 10, 20, 0.1, 0.05),
            new EvolutionaryAlgorithm(1000, 10, 20, 0.2, 0.05),
            new EvolutionaryAlgorithm(1000, 10, 20, 0.3, 0.05),
            new EvolutionaryAlgorithm(1000, 10, 20, 0.4, 0.05),
            new EvolutionaryAlgorithm(1000, 10, 20, 0.5, 0.05),
            new EvolutionaryAlgorithm(1000, 10, 20, 0.6, 0.05),
            new EvolutionaryAlgorithm(1000, 10, 20, 0.7, 0.05),
            new EvolutionaryAlgorithm(1000, 10, 20, 0.8, 0.05),
            new EvolutionaryAlgorithm(1000, 10, 20, 0.9, 0.05),

            new EvolutionaryAlgorithm(1000, 10, 20, 0.7, 0),
            new EvolutionaryAlgorithm(1000, 10, 20, 0.7, 0.05),
            new EvolutionaryAlgorithm(1000, 10, 20, 0.7, 0.1),
            new EvolutionaryAlgorithm(1000, 10, 20, 0.7, 0.15),
            new EvolutionaryAlgorithm(1000, 10, 20, 0.7, 0.2),
            new EvolutionaryAlgorithm(1000, 10, 20, 0.7, 0.25),
            new EvolutionaryAlgorithm(1000, 10, 20, 0.7, 0.3),
            new EvolutionaryAlgorithm(1000, 10, 20, 0.7, 0.35),
            new EvolutionaryAlgorithm(1000, 10, 20, 0.7, 0.4),
            new EvolutionaryAlgorithm(1000, 10, 20, 0.7, 0.45),
            new EvolutionaryAlgorithm(1000, 10, 20, 0.7, 0.5),

//            new SimulatedAnnealing(0.999, 1000, 10, 1),
//            new SimulatedAnnealing(0.999, 10000, 0.001, 1),
//
//            new RandomSearch(1000),
        };

        String[] instances = {
                "berlin52_n51_bounded-strongly-corr_01.ttp",
                "berlin52_n51_uncorr-similar-weights_01.ttp",
                "eil51_n50_uncorr_01.ttp",
                "eil51_n150_uncorr-similar-weights_01.ttp",
                "rd100_n99_bounded-strongly-corr_01.ttp",
                "rd100_n99_uncorr_01.ttp",
                "simple4_n6_02.ttp"
        };


        createComparisonFile(FILENAME);
        final int RUN_AMOUNT = 10;

        for (String instance : instances) {
            ArrayList<Solution> results;
            Problem.setupInstance("src/problems/" + instance, null);

            for (Optimizer newStrategy : otherStrategies) {
                results = new ArrayList<>(RUN_AMOUNT);
                for (int i = 0; i < RUN_AMOUNT; i++) {
                    newStrategy.reset();
                    Problem.changeStrategy(newStrategy);
                    Solution result = Problem.solve();
                    results.add(result);
                }
                double min = results.stream().mapToDouble(Solution::getFitness).min().getAsDouble();
                double max = results.stream().mapToDouble(Solution::getFitness).max().getAsDouble();
                double average = results.stream().mapToDouble(Solution::getFitness).average().getAsDouble();
                double stdDev = Math.sqrt(results.stream().mapToDouble(Solution::getFitness).map(x -> Math.pow(x - average, 2)).sum() / RUN_AMOUNT);

                writeToFile(FILENAME, instance + "," + newStrategy + "," + min + "," + max + "," + average + "," + stdDev + "\n");
            }

            for (Optimizer greedyStrategy : greedyStrategies) {
                Problem.changeStrategy(greedyStrategy);
                Solution result = Problem.solve();
                StringBuilder contents = new StringBuilder(instance + " " + greedyStrategy + ",");
                for (int i = 0; i < 3; i++) {
                    contents.append(result.getFitness()).append(",");
                }
                contents.append(0.0).append('\n');
                writeToFile(FILENAME, contents.toString());
            }
        }
    }

    static void createComparisonFile(String fileName){
        File file = new File("results/" + fileName);

        try (FileWriter fw = new FileWriter(file, false)){
            file.createNewFile();
            String[] headers = {"Instance", "Name", "Min", "Max", "Average", "Standard Deviation\n"};
            String header = String.join(",", headers);
            fw.append(header);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void writeToFile(String fileName, String contents){
        File file = new File("results/" + fileName);
        try (FileWriter fw = new FileWriter(file, true)){
            fw.append(contents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}