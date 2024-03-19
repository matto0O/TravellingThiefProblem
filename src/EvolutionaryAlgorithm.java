import java.util.ArrayList;
import java.util.Arrays;
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
        System.out.println(contestants.getLast());

        return contestants.getLast();
    }

    private double calculateTime(double minSpeed, double maxSpeed, double distance, Knapsack knapsack, Item item){
        int itemWeight = item == null ? 0 : item.getWeight();

        return distance /
                (maxSpeed - (maxSpeed - minSpeed) * (knapsack.getWeight() + itemWeight) /
                knapsack.getCapacity());
    }

    private Solution crossover(ArrayList<Solution> population, double minSpeed,
                               double maxSpeed, double[][] distanceMatrix){
        Solution parent1 = tournament();
        Solution parent2 = tournament();

        City currentOffspringCity = parent1.getCities().getFirst();
        Knapsack offspringKnapsack = new Knapsack(parent1.getKnapsack().getCapacity());

        Solution offspring = new Solution(offspringKnapsack, currentOffspringCity);

        int citiesLen = parent1.getCities().size();
        int citySubsequenceStartIndex = random.nextInt(citiesLen - 1) + 1;
        int citySubsequenceEndIndex = random.nextInt(citiesLen - citySubsequenceStartIndex) + citySubsequenceStartIndex;
        System.out.println(citySubsequenceStartIndex);
        System.out.println(citySubsequenceEndIndex);
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
        }
//        for (int i=1; i<citiesLen; i++){
//            Solution selectedParent = random.nextDouble() > crossoverChance ? parent1 : parent2;
//
//            City city = selectedParent.getCities().get(i);
//            Item selectedItem = selectedParent.itemStolenFromCity(city);
//            offspring.appendSolution(
//                    city,
//                    calculateTime(
//                            minSpeed,
//                            maxSpeed,
//                            distanceMatrix[city.getIndex()-1][currentOffspringCity.getIndex()-1],
//                            offspringKnapsack,
//                            selectedItem
//                    ),
//                    selectedItem
//            );
//        }

        return offspring;
    }

    private Solution mutation(Solution solution){
        return null;
    }

    @Override
    public Solution solve(City[] cities, int knapsackSize, double minSpeed,
                          double maxSpeed, double[][] distanceMatrix, int iterations) {

        RandomSearch rs = new RandomSearch();
        for (int i=0; i<populationSize; i++){
            population.add(rs.solve(cities, knapsackSize, minSpeed, maxSpeed, distanceMatrix, 1));
        }

        Solution bestSolution = null;
//        for (int i=0; i<iterations; i++){
            bestSolution = crossover(population, minSpeed, maxSpeed, distanceMatrix);
//        }
//        bestSolution = tournament();
        return bestSolution;
    }
}
