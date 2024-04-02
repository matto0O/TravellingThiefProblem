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

        Problem.putRunSummary(runSummary(0));
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Evolutionary Algorithm ");
        for (Map.Entry<String, Number> entry : params().entrySet()){
            sb.append(entry.getKey()).append("-").append(entry.getValue()).append(" ");
        }

        return sb.toString();
    }

    @Override
    public void reset(){
        population.clear();
        fitnessOverTime.clear();
    }
}
