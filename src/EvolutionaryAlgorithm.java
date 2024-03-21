import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class EvolutionaryAlgorithm implements Optimizer{

    private final int populationSize, tournamentSize, iterations;
    private final double crossoverChance, mutationChance;
    private final ArrayList<Solution> population;
    private final ArrayList<Double[]> fitnessOverTime;

    public EvolutionaryAlgorithm(int populationSize, int tournamentSize, int iterations,
                                 double crossoverChance, double mutationChance){
        this.populationSize = populationSize;
        this.tournamentSize = tournamentSize;
        this.iterations = iterations;
        this.crossoverChance = crossoverChance;
        this.mutationChance = mutationChance;
        population = new ArrayList<>(populationSize);
        fitnessOverTime = new ArrayList<>(iterations);
    }

    private Solution tournament(){
        if (populationSize < tournamentSize){
            throw new IllegalArgumentException(
                    "Tournament size cannot exceed population size (" + populationSize +")."
            );
        }

        ArrayList<Solution> contestants = new ArrayList<>(tournamentSize);
        while (contestants.size() < tournamentSize){
            contestants.add(population.get(Problem.random.nextInt(populationSize)));
        }

        contestants.sort(new FitnessComparator());

        return contestants.getLast();
    }

    @Override
    public Solution solve() {

        RandomSearch rs = new RandomSearch(1);
        for (int i=0; i<populationSize; i++){
            population.add(rs.solve());
        }

        population.sort(new FitnessComparator());
        Solution bestSolution = population.getLast();

        for (int i=0; i<iterations; i++){
            putIterationFitness();
            ArrayList<Solution> newPopulation = new ArrayList<>(populationSize);

            while(newPopulation.size() < populationSize) {
                Solution parent1 = tournament();
                Solution parent2 = tournament();
                Solution postCross = Operators.crossover(parent1, parent2, crossoverChance);
                if (postCross != null) {
                    postCross = Operators.mutation(postCross, mutationChance);
                    postCross.calculateFitness();
                    newPopulation.add(postCross);
                }
            }

            population.clear();
            population.addAll(newPopulation);
            population.sort(new FitnessComparator());
            bestSolution = population.getLast().getFitness() > bestSolution.getFitness() ? population.getLast() : bestSolution;
        }

        return bestSolution;
    }

    private void putIterationFitness(){
        Double[] iterationFitness = new Double[populationSize];
        for (int i=0; i<populationSize; i++){
            iterationFitness[i] = population.get(i).getFitness();
        }
        fitnessOverTime.add(iterationFitness);
    }

    @Override
    public boolean saveToFile(String fileName, int runNumber) {
        String[] data = new String[6];

        HashMap<String, Number> params = params();
        StringBuilder sb = new StringBuilder();
        sb.append("Evolutionary Algorithm - ");

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
    public HashMap<String, Number> params() {
        HashMap<String, Number> result = new HashMap<>(5);

        result.put("p", populationSize);
        result.put("t", tournamentSize);
        result.put("c", crossoverChance);
        result.put("m", mutationChance);
        result.put("i", iterations);

        return result;
    }

    @Override
    public Number[] runSummary(int runNumber) {
        Number[] result = new Number[5];

        ArrayList<Double> fitness = new ArrayList<>();
        for (Double[] iteration : fitnessOverTime){
            Collections.addAll(fitness, iteration);
        }

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
    public String iterationDetails(){
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<populationSize; i++){
            sb.append(i).append(",");
        }
        sb.append("\n");
        for (Double[] iteration : fitnessOverTime){
            for (Double fitness : iteration){
                sb.append(fitness).append(",");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public String iterationPreview(){
        StringBuilder sb = new StringBuilder();
        sb.append("Iteration,Min,Max,Mean,StD\n");
        for (int i=0; i<iterations; i++){

            ArrayList<Double> fitness;
            Double[] iteration = fitnessOverTime.get(i);
            fitness = new ArrayList<>(Arrays.stream(iteration).toList());

            sb.append(i).append(",");
            sb.append(Collections.min(fitness)).append(",");
            sb.append(Collections.max(fitness)).append(",");
            sb.append(fitness.stream().mapToDouble(Double::doubleValue).average().orElse(0)).append(",");
            ArrayList<Double> finalFitness = fitness;
            sb.append(Math.sqrt(fitness.stream().mapToDouble(Double::doubleValue).map(
                    x -> Math.pow(x - finalFitness.stream().mapToDouble(Double::doubleValue).average().orElse(0), 2)).sum()
                    / fitness.size())).append("\n");

        }
        return sb.toString();
    }

    @Override
    public Double[][] iterationNumbersPreview() {

        Double[][] result = new Double[iterations][4];

        for (int i=0; i<iterations; i++){

            ArrayList<Double> fitness;
            Double[] iteration = fitnessOverTime.get(i);
            fitness = new ArrayList<>(Arrays.stream(iteration).toList());

            result[i][0] = Collections.min(fitness);
            result[i][1] = Collections.max(fitness);
            result[i][2] = fitness.stream().mapToDouble(Double::doubleValue).average().orElse(0);
            ArrayList<Double> finalFitness = fitness;
            result[i][3] = Math.sqrt(fitness.stream().mapToDouble(Double::doubleValue).map(
                    x -> Math.pow(x - finalFitness.stream().mapToDouble(Double::doubleValue).average().orElse(0), 2)).sum()
                    / fitness.size());

        }

        return result;
    }
}
