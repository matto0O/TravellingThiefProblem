import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RandomSearch implements Optimizer{
    @Override
    public Solution solve(City[] cities, int knapsackSize, double minSpeed,
                          double maxSpeed, double[][] distanceMatrix, int iterations) {

        Random random = new Random();
        Solution bestSolution = null;

        for (int i=0; i<iterations; i++){
            Knapsack knapsack = new Knapsack(knapsackSize);
            ArrayList<City> unvisitedCities = new ArrayList<>(Arrays.asList(cities));

            City currentCity = unvisitedCities.removeFirst();

            Solution solution = new Solution(knapsack, currentCity);

            while (!unvisitedCities.isEmpty()) {
                City nextCity = unvisitedCities.get(random.nextInt(unvisitedCities.size()));
                int maxWeight = knapsack.getCapacity() - knapsack.getWeight();
                ArrayList<Item> items = nextCity.getItemsLighterThan(maxWeight);
                Item selectedItem = !items.isEmpty() ? items.get(random.nextInt(items.size())) : null;

                double time;

                if(selectedItem != null) {
                    time = distanceMatrix[currentCity.getIndex()-1][nextCity.getIndex()-1] /
                            (maxSpeed - (maxSpeed - minSpeed) * (knapsack.getWeight() + selectedItem.getWeight()) /
                                    knapsack.getCapacity());
                } else {
                    time = distanceMatrix[currentCity.getIndex()-1][nextCity.getIndex()-1] /
                            (maxSpeed - (maxSpeed - minSpeed) * (knapsack.getWeight()) /
                                    knapsack.getCapacity());
                }

                solution.appendSolution(nextCity, time, selectedItem);

                currentCity = nextCity;
                unvisitedCities.remove(currentCity);
            }

            if (bestSolution == null || bestSolution.getFitness() < solution.getFitness()){
                bestSolution = solution;
            }

//            System.out.println("Iteration " + i + " - Best solution: " + bestSolution.getFitness() +
//                    " - Current solution: " + solution.getFitness());
        }

        return bestSolution;
    }
}
