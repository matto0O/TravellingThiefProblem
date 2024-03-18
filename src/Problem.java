import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Problem {
    private final String fileName;
    private City[] cities;
    private double[][] distanceMatrix;
    private int knapsackSize;
    private double minSpeed, maxSpeed, coefficient;
    private final Optimizer strategy;

    public Problem(String fileName, Optimizer strategy) {
        this.fileName = fileName;
        this.strategy = strategy;
        loadProblem();
        calculateDistanceMatrix();
    }

    private void loadProblem(){
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

    private void calculateDistanceMatrix(){
        distanceMatrix = new double[cities.length][cities.length];
        for (int i = 0; i < cities.length; i++) {
            for (int j = 0; j < cities.length; j++) {
                if (i!=j)
                    distanceMatrix[i][j] = cities[i].distanceTo(cities[j]) * coefficient;
            }
        }
    }

    public void printCities(){
        System.out.println("Cities:");
        for (City city : cities) {
            System.out.println(city);
        }
    }

    public Solution solve(){
        return strategy.solve(cities, knapsackSize, minSpeed, maxSpeed, distanceMatrix, 100);
    }
}
