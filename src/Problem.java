import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Problem {
    private static Problem instance = null;

    static String fileName;
    static City[] cities;
    static double[][] distanceMatrix;
    static int knapsackSize;
    static double minSpeed, maxSpeed, coefficient;
    private static Optimizer strategy;
    static Random random;

    private Problem(String fileName, Optimizer strategy) {
        Problem.fileName = fileName;
        Problem.strategy = strategy;
        random = new Random();
        loadProblem();
        calculateDistanceMatrix();
    }

    public static synchronized void changeStrategy(Optimizer newStrategy){
        strategy = newStrategy;
    }

    public static synchronized Problem setupInstance(String fileName, Optimizer strategy) {
        instance = new Problem(fileName, strategy);
        return instance;
    }

    public static synchronized Problem getInstance() {
        return instance;
    }

    public static synchronized double distanceBetween(City city1, City city2) {
        return distanceMatrix[city1.getIndex() - 1][city2.getIndex() - 1];
    }

    public static synchronized double timeBetween(City city1, City city2, Knapsack knapsack) {
        double distance = distanceBetween(city1, city2);

        double speed = maxSpeed - ((double) knapsack.getWeight() / knapsack.getCapacity() * (maxSpeed - minSpeed));
        return distance / speed;
    }

    private static void loadProblem(){
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);

            myReader.nextLine();
            myReader.nextLine();

            String[] line2 = myReader.nextLine().split("\t");
            cities = new City[Integer.parseInt(line2[1])];

            myReader.nextLine();

            String[] line4 = myReader.nextLine().split("\t");
            knapsackSize = Integer.parseInt(line4[1]);

            String[] line5 = myReader.nextLine().split("\t");
            minSpeed = Double.parseDouble(line5[1]);

            String[] line6 = myReader.nextLine().split("\t");
            maxSpeed = Double.parseDouble(line6[1]);

            String[] line7 = myReader.nextLine().split("\t");
            coefficient = Double.parseDouble(line7[1]);

            myReader.nextLine();
            myReader.nextLine();

            for (int i=0; i<cities.length; i++) {
                String[] data = myReader.nextLine().split("\t");
                int cityIndex = Integer.parseInt(data[0]);
                double x = Double.parseDouble(data[1]);
                double y = Double.parseDouble(data[2]);
                cities[cityIndex - 1] = new City(cityIndex, x, y);
            }

            myReader.nextLine();

            while(myReader.hasNextLine()) {
                String[] data = myReader.nextLine().split("\t");
                int itemIndex = Integer.parseInt(data[0]);
                int profit = Integer.parseInt(data[1]);
                int weight = Integer.parseInt(data[2]);
                int assignedNode = Integer.parseInt(data[3]);
                cities[assignedNode - 1].addItem(new Item(itemIndex, profit, weight));
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void calculateDistanceMatrix(){
        distanceMatrix = new double[cities.length][cities.length];
        for (int i = 0; i < cities.length; i++) {
            for (int j = 0; j < cities.length; j++) {
                if (i!=j)
                    distanceMatrix[i][j] = cities[i].distanceTo(cities[j]) * coefficient;
            }
        }
    }

    public static void printCities(){
        System.out.println("Cities:");
        for (City city : cities) {
            System.out.println(city);
        }
    }

    public static Solution solve(){
        return strategy.solve();
    }

    public static void saveToFile(String fileName, int runNumber){
        strategy.saveToFile(fileName, runNumber);
    }

    public static String iterationDetails(){
        return strategy.iterationDetails();
    }

    public static String iterationPreview(){
        return strategy.iterationPreview();
    }
}
