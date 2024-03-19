import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EvolutionaryAlgorithm implements Optimizer{

    private final int populationSize, tournamentSize;
    private final double crossoverChance, mutationChance;
    private final ArrayList<Solution> population;
    private final Random random;

    public EvolutionaryAlgorithm(int populationSize, int tournamentSize,
                                 double crossoverChance, double mutationChance){
        this.populationSize = populationSize;
        this.tournamentSize = tournamentSize;
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

    private double calculateTime(double minSpeed, double maxSpeed, double distance, Knapsack knapsack, Item item){
        int itemWeight = item == null ? 0 : item.getWeight();

        return distance /
                (maxSpeed - (maxSpeed - minSpeed) * (knapsack.getWeight() + itemWeight) /
                knapsack.getCapacity());
    }

    private Solution crossover(double minSpeed, double maxSpeed, double[][] distanceMatrix){
        Solution parent1 = tournament();
        Solution parent2 = tournament();

        if(random.nextDouble() > crossoverChance){
            return null;
        }

        City currentOffspringCity = parent1.getCities().getFirst();
        Knapsack offspringKnapsack = new Knapsack(parent1.getKnapsack().getCapacity());

        Solution offspring = new Solution(offspringKnapsack, currentOffspringCity);

        int citiesLen = parent1.getCities().size();
        int citySubsequenceStartIndex = random.nextInt(citiesLen - 1) + 1;
        int citySubsequenceEndIndex = random.nextInt(citiesLen - citySubsequenceStartIndex) + citySubsequenceStartIndex;

        List<City> cities = parent1.getCities()
                .subList(citySubsequenceStartIndex, citySubsequenceEndIndex+1);

        ArrayList<City> p2cities = new ArrayList<>(parent2.getCities());
        p2cities.removeFirst();
        p2cities.removeAll(cities);

        while(offspring.getCities().size() < citySubsequenceStartIndex)
        {
            City city;
            do {
                city = p2cities.removeFirst();
            } while (cities.contains(city));

            Item selectedItem = parent2.itemStolenFromCity(city);

            offspring.appendSolution(
                    city,
                    calculateTime(
                            minSpeed,
                            maxSpeed,
                            distanceMatrix[city.getIndex()-1][currentOffspringCity.getIndex()-1],
                            offspringKnapsack,
                            selectedItem
                    ),
                    selectedItem
            );

            currentOffspringCity = city;
        }

        for(int i=citySubsequenceStartIndex; i<=citySubsequenceEndIndex; i++){
            City city = parent1.getCities().get(i);
            Item selectedItem = parent1.itemStolenFromCity(city);

            offspring.appendSolution(
                    city,
                    calculateTime(
                            minSpeed,
                            maxSpeed,
                            distanceMatrix[city.getIndex()-1][currentOffspringCity.getIndex()-1],
                            offspringKnapsack,
                            selectedItem
                    ),
                    selectedItem
            );

            currentOffspringCity = city;
        }

        while(!p2cities.isEmpty())
        {
            City city = p2cities.removeFirst();

            Item selectedItem = parent2.itemStolenFromCity(city);

            offspring.appendSolution(
                    city,
                    calculateTime(
                            minSpeed,
                            maxSpeed,
                            distanceMatrix[city.getIndex()-1][currentOffspringCity.getIndex()-1],
                            offspringKnapsack,
                            selectedItem
                    ),
                    selectedItem
            );
            currentOffspringCity = city;
        }

        return offspring;
    }

    private Solution mutation(Solution solution, double minSpeed, double maxSpeed, double[][] distanceMatrix){
        // INVERSE MUTATION

        if(random.nextDouble() > mutationChance){
            return solution;
        }

        Solution offspring = solution.copy();

        int citiesLen = solution.getCities().size();
        int citySubsequenceStartIndex = random.nextInt(citiesLen);
        int citySubsequenceEndIndex = random.nextInt(citiesLen - citySubsequenceStartIndex) + citySubsequenceStartIndex + 1;

        System.out.println("Before mutation: " + offspring);
        offspring.invert(citySubsequenceStartIndex, citySubsequenceEndIndex);       //TODO proper logs, but working
        System.out.println("After mutation: " + offspring);

        return offspring;
    }

    @Override
    public Solution solve(City[] cities, int knapsackSize, double minSpeed,
                          double maxSpeed, double[][] distanceMatrix, int iterations) {

        RandomSearch rs = new RandomSearch();
        for (int i=0; i<populationSize; i++){
            population.add(rs.solve(cities, knapsackSize, minSpeed, maxSpeed, distanceMatrix, 1));
        }

        population.sort(new FitnessComparator());
        Solution bestSolution = population.getLast();

        for (int i=0; i<iterations; i++){
            ArrayList<Solution> newPopulation = new ArrayList<>(populationSize);

            while(newPopulation.size() < populationSize) {
                Solution postCross = crossover(minSpeed, maxSpeed, distanceMatrix);
                if (postCross != null) {
                    postCross = mutation(postCross, minSpeed, maxSpeed, distanceMatrix);
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
