import java.util.ArrayList;
import java.util.Random;

public class EvolutionaryAlgorithm implements Optimizer{

    private final int populationSize, tournamentSize, iterations;
    private final double crossoverChance, mutationChance;
    private final ArrayList<Solution> population;
    private final Random random;

    public EvolutionaryAlgorithm(int populationSize, int tournamentSize, int iterations,
                                 double crossoverChance, double mutationChance){
        this.populationSize = populationSize;
        this.tournamentSize = tournamentSize;
        this.iterations = iterations;
        this.crossoverChance = crossoverChance;
        this.mutationChance = mutationChance;
        population = new ArrayList<>(populationSize);
        random = new Random();
    }

    private Solution tournament(){
        if (populationSize < tournamentSize){
            throw new IllegalArgumentException(
                    "Tournament size cannot exceed population size (" + populationSize +")."
            );
        }

        ArrayList<Solution> contestants = new ArrayList<>(tournamentSize);
        while (contestants.size() < tournamentSize){
            contestants.add(population.get(random.nextInt(populationSize)));
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
}
