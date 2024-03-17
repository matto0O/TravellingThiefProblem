import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RandomSearch implements Optimizer{
    @Override
    public Solution solve(City[] cities, Knapsack knapsack, double minSpeed,
                          double maxSpeed, double[][] distanceMatrix, int iterations) {

        Random random = new Random();
        Solution bestSolution = null;

        for (int i=0; i<iterations; i++){
            Knapsack copyKnapsack = new Knapsack(knapsack.getCapacity());
            ArrayList<City> unvisitedCities = new ArrayList<>(Arrays.asList(cities));

            City currentCity = unvisitedCities.remove(0);

            Solution solution = new Solution(copyKnapsack, currentCity);

            while (!unvisitedCities.isEmpty()) {
                City nextCity = unvisitedCities.get(random.nextInt(unvisitedCities.size()));
                int maxWeight = copyKnapsack.getCapacity() - copyKnapsack.getWeight();
                ArrayList<Item> items = nextCity.getItemsLighterThan(maxWeight);
                Item selectedItem = items.size() > 0 ? items.get(random.nextInt(items.size())) : null;

                double time;

                if(selectedItem != null) {
                    time = distanceMatrix[currentCity.getIndex()-1][nextCity.getIndex()-1] /
                            (maxSpeed - (maxSpeed - minSpeed) * (copyKnapsack.getWeight() + selectedItem.getWeight()) /
                                    copyKnapsack.getCapacity());
                } else {
                    time = distanceMatrix[currentCity.getIndex()-1][nextCity.getIndex()-1] /
                            (maxSpeed - (maxSpeed - minSpeed) * (copyKnapsack.getWeight()) /
                                    copyKnapsack.getCapacity());
                }

                solution.appendSolution(nextCity, time, selectedItem);

                currentCity = nextCity;
                unvisitedCities.remove(currentCity);
            }

            if (bestSolution == null || bestSolution.getFitness() < solution.getFitness()){
                bestSolution = solution;
            }

            System.out.println("Iteration " + i + " - Best solution: " + bestSolution.getFitness() +
                    " - Current solution: " + solution.getFitness());
        }
        System.out.println(bestSolution);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return bestSolution;
    }
}
