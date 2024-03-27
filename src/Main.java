import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        String FILENAME = "results7.csv";

        Optimizer[] greedyStrategies = {
//                new GreedyAlgorithm(),
                new GreedyAlgorithmAllStarts(),
//                new MySA(0.999, 10000, 0.001, 1),
                new SimulatedAnnealing(0.999, 10000, 0.001, 1),
        };

        Optimizer[] otherStrategies = {
//            new EvolutionaryAlgorithm(100, 10, 100, 0.7, 0.05),
//            new EvolutionaryAlgorithm(1000, 10, 100, 0.7, 0.05),
//            new EvolutionaryAlgorithm(1000, 25, 100, 0.7, 0.05),
            new EvolutionaryAlgorithm(1000, 25, 100, 0.7, 0.2),
            new EvolutionaryAlgorithm(1000, 25, 100, 0.5, 0.05),
//            new EvolutionaryAlgorithm(1000, 25, 1000, 0.7, 0.05),
//            new EvolutionaryAlgorithm(100, 10, 10000, 0.7, 0.1),

//            new SimulatedAnnealing(0.99, 1000, 0.001, 1),
//            new SimulatedAnnealing(0.99, 1000, 0.1, 1),

//            new MySA(0.99, 1000, 0.001, 1),
//            new MySA(0.99, 1000, 0.1, 1),
//
//            new RandomSearch(1000),
            new RandomSearch(10000),
        };
        Problem.setupInstance(
                "src/problems/rd100_n99_bounded-strongly-corr_01.ttp", null);

        createComparisonFile(FILENAME);

        for (int j=0; j< otherStrategies.length; j++) {
            Problem.changeStrategy(otherStrategies[j]);
            for (int i = 0; i < 10; i++) {
                Problem.solve();
                Problem.saveToFile("results/" + FILENAME, i);
//                createRunFile(j + "_" +i +".csv",Problem.iterationPreview());
            }
        }

        for (int j=0; j< greedyStrategies.length; j++) {
            Problem.changeStrategy(greedyStrategies[j]);
            Problem.solve();
            Problem.saveToFile("results/" + FILENAME, 0);
//            createRunFile((otherStrategies.length +j) +".csv",Problem.iterationPreview());
        }
    }

    static void createComparisonFile(String fileName){
        File file = new File("results/" + fileName);

        try (FileWriter fw = new FileWriter(file, false)){
            file.createNewFile();
            String[] headers = {"Name", "Run", "Min", "Max", "Average", "Standard Deviation\n"};
            String header = String.join(",", headers);
            fw.append(header);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void createRunFile(String fileName, String contents){
        File file = new File("results/" + fileName);

        try (FileWriter fw = new FileWriter(file, false)){
            file.createNewFile();
            fw.append(contents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}