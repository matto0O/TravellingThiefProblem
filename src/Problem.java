import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Problem {
    private final String fileName;
    private City[] cities;
    private Knapsack knapsack;
    private double minSpeed, maxSpeed;
    private final Optimizer strategy;

    public Problem(String fileName, Optimizer strategy) {
        this.fileName = fileName;
        this.strategy = strategy;
        loadProblem();
    }

    private void loadProblem(){
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            int line = 0;
            boolean citiesInitialized = false;
            boolean knapsackInitialized = false;
            while (myReader.hasNextLine()) {
                String[] data = myReader.nextLine().split("\t");
                if (!citiesInitialized && line == 2) {
                    cities = new City[Integer.parseInt(data[1])];
                    citiesInitialized = true;
                } else if (!knapsackInitialized && line == 4) {
                    knapsack = new Knapsack(Integer.parseInt(data[1]));
                    knapsackInitialized = true;
                } else if (knapsackInitialized && line == 5) {
                    minSpeed = Double.parseDouble(data[1]);
                } else if (knapsackInitialized && line == 6) {
                    maxSpeed = Double.parseDouble(data[1]);
                }
                else if (citiesInitialized && line >= 10 && line < 10 + cities.length){
                    int cityIndex = Integer.parseInt(data[0]);
                    double x = Double.parseDouble(data[1]);
                    double y = Double.parseDouble(data[2]);
                    cities[cityIndex - 1] = new City(cityIndex, x, y);
                }
                else if (citiesInitialized && line >= 11 + cities.length){
                    int itemIndex = Integer.parseInt(data[0]);
                    int profit = Integer.parseInt(data[1]);
                    int weight = Integer.parseInt(data[2]);
                    int assignedNode = Integer.parseInt(data[3]);
                    cities[assignedNode - 1].addItem(new Item(itemIndex, profit, weight));
                }
                line++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public void print(){
        System.out.println("Cities:");
        for (City city : cities) {
            System.out.println(city);
        }
    }

    public void solve(){
        strategy.solve(cities, knapsack, minSpeed, maxSpeed);
    }
}
