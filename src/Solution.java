import java.util.ArrayList;
import java.util.List;

public class Solution implements Comparable<Solution>{

    private final Knapsack knapsack;
    private final ArrayList<City> cities;
    private ArrayList<String> logs;
    private double fitness;
    private double time;

    public Solution(Knapsack knapsack, City startCity) {
        this.knapsack = knapsack;
        this.cities = new ArrayList<>();
        if(startCity != null)
            this.cities.add(startCity);
    }

    public Solution copy(){
        Solution copy = new Solution(knapsack.copy(), null);
        copy.fitness = fitness;
        copy.cities.addAll(cities);
        return copy;
    }

    public void invert(int start, int end){
        List<City> subList = new ArrayList<>(cities.subList(start, end)).reversed();
        for(int i = start; i < end; i++){
            cities.set(i, subList.get(i - start));
        }
    }

    public double getFitness() {
        return fitness;
    }

    public ArrayList<City> getCities() {return cities;}

    public Knapsack getKnapsack() {return knapsack;}

    public boolean calculateFitness(double minSpeed, double maxSpeed, double[][] distanceMatrix){
        double newFitness = 0.0;
        logs = new ArrayList<>();

        City previousCity = cities.getFirst();
        logs.add("First city - " + previousCity.getIndex());
        Knapsack tempKnapsack = new Knapsack(knapsack.getCapacity());
        double globalTime = 0.0;
        for (City city: cities){
            double distance = distanceMatrix[previousCity.getIndex() - 1][city.getIndex() - 1];
            if (distance == 0.0) continue;
            double speed = distance / (maxSpeed - (maxSpeed - minSpeed) *
                    knapsack.getWeight() / knapsack.getCapacity());

            double localtime = speed / distance;

            newFitness -= localtime;
            globalTime += localtime;

            String log = "\nCity nr " + city.getIndex();

            Item itemStolen = itemStolenFromCity(city);
            if(itemStolen != null) {
                try {
                    tempKnapsack.putItem(itemStolen);
                    newFitness += itemStolen.getProfit();
                    log += " Stolen " + itemStolen;
                } catch (IllegalArgumentException ignored){}
            }
            log += " knapsack - " + knapsack.getWeight() + "/" + knapsack.getCapacity();
            logs.add(log);
            previousCity = city;
        }

        boolean equal = newFitness == fitness && time == globalTime;
        fitness = newFitness;
        time = globalTime;
        return equal;
    }

    public void appendSolution(City city, double time, Item item) {
        if(item != null) {
            try{
                knapsack.putItem(item);
                fitness += item.getProfit();
            } catch (IllegalArgumentException ignored){}
        }
        cities.add(city);
        fitness -= time;
        this.time += time;
    }

    public Item itemStolenFromCity(City city){
        ArrayList<Item> itemsInCity = city.getItems();
        for(Item item: knapsack.getItems()){
            if(itemsInCity.contains(item)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public int compareTo(Solution o) {
        return Double.compare(this.fitness, o.fitness);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Fitness: ").append(fitness).append("\n");
//        sb.append("Cities: ");
//        for(int i = 0; i < cities.size(); i++) {
//            sb.append(cities.get(i).getIndex()).append(", weight - ").append(knapsack.getWeight()).append("\n");
//        }
//        sb.append(cities.getLast().getIndex()).append("\n");
        for (String log: logs){
            sb.append(log);
        }
        sb.append("\nTime: ").append(time).append("\n");
//        sb.append("Knapsack value ").append(knapsack.get()).append("\n");

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Solution other)) return false;
        return this.cities.equals(other.cities) && this.knapsack.equals(other.knapsack);
    }
}
