import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RandomSearch implements Optimizer{
    private final int iterations;

    public RandomSearch(int iterations){
        this.iterations = iterations;
    }

    @Override
    public Solution solve() {

        Random random = new Random();
        Solution bestSolution = null;

        for (int i=0; i<iterations; i++){
            Knapsack knapsack = new Knapsack(Problem.knapsackSize);
            ArrayList<City> unvisitedCities = new ArrayList<>(Arrays.asList(Problem.cities));

            City currentCity = unvisitedCities.remove(random.nextInt(unvisitedCities.size()));

            Solution solution = new Solution(knapsack, currentCity);

            while (!unvisitedCities.isEmpty()) {
                City nextCity = unvisitedCities.get(random.nextInt(unvisitedCities.size()));
                int maxWeight = knapsack.getCapacity() - knapsack.getWeight();
                ArrayList<Item> items = nextCity.getItemsLighterThan(maxWeight);
                int randomIndex = random.nextInt(items.size() + 1);
                Item selectedItem = items.isEmpty() || randomIndex == items.size() ? null : items.get(randomIndex);

                solution.appendSolution(nextCity, selectedItem);

                currentCity = nextCity;
                unvisitedCities.remove(currentCity);
            }

            solution.calculateFitness();

            if (bestSolution == null || bestSolution.getFitness() < solution.getFitness()){
                bestSolution = solution;
            }
        }
        if (bestSolution != null) {
            bestSolution.calculateFitness();
        }

        return bestSolution;
    }
}
