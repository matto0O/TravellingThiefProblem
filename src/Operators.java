import java.util.ArrayList;
import java.util.List;

public class Operators {
    public static Solution crossover(Solution parent1, Solution parent2, double crossoverChance){
        // ORDER CROSSOVER

        if(Problem.random.nextDouble() > crossoverChance){
            return null;
        }

        Knapsack offspringKnapsack = new Knapsack(parent1.getKnapsack().getCapacity());

        Solution offspring = new Solution(offspringKnapsack, null);

        int citiesLen = parent1.getCities().size();
        int citySubsequenceStartIndex = Problem.random.nextInt(citiesLen - 1) + 1;
        int citySubsequenceEndIndex = Problem.random.nextInt(citiesLen - citySubsequenceStartIndex) + citySubsequenceStartIndex;

        List<City> cities = parent1.getCities()
                .subList(citySubsequenceStartIndex, citySubsequenceEndIndex+1);

        ArrayList<City> p2cities = new ArrayList<>(parent2.getCities());
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
                    selectedItem
            );
        }

        for(int i=citySubsequenceStartIndex; i<=citySubsequenceEndIndex; i++){
            City city = parent1.getCities().get(i);
            Item selectedItem = parent1.itemStolenFromCity(city);

            offspring.appendSolution(
                    city,
                    selectedItem
            );
        }

        while(!p2cities.isEmpty())
        {
            City city = p2cities.removeFirst();

            Item selectedItem = parent2.itemStolenFromCity(city);

            offspring.appendSolution(
                    city,
                    selectedItem
            );
        }

        offspring.calculateFitness();
        return offspring;
    }

    public static Solution mutation(Solution solution, double mutationChance){
        // INVERSE MUTATION

        if(Problem.random.nextDouble() > mutationChance){
            return solution;
        }

        Solution offspring = solution.copy();

        int citiesLen = solution.getCities().size();
        int citySubsequenceStartIndex = Problem.random.nextInt(citiesLen);
        int citySubsequenceEndIndex = Problem.random.nextInt(citiesLen - citySubsequenceStartIndex) + citySubsequenceStartIndex + 1;

        offspring.invert(citySubsequenceStartIndex, citySubsequenceEndIndex);
        offspring.calculateFitness();

        return offspring;
    }
}
