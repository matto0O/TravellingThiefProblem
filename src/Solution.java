import java.util.ArrayList;
import java.util.List;

public class Solution implements Comparable<Solution>{

    private final Knapsack knapsack;
    private final ArrayList<City> cities;
    private final ArrayList<String> logs;
    private double fitness;
    private double time;

    public Solution(Knapsack knapsack, City startCity) {
        this.knapsack = knapsack;
        this.cities = new ArrayList<>();
        this.cities.add(startCity);
        this.logs = new ArrayList<>();
    }

    public Solution copy(){
        Solution copy = new Solution(knapsack.copy(), null);
        copy.fitness = fitness;
        copy.cities.clear();
        copy.cities.addAll(cities);
        copy.logs.addAll(logs);
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

    public void appendSolution(City city, double time, Item item) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cities.size()).
            append(". travel to city nr").append(city.getIndex()).append(" ");

        if(item != null) {
            try{
                knapsack.putItem(item);
                fitness += item.getProfit();
                stringBuilder.append("Item stolen: ").append(item);
            } catch (IllegalArgumentException ignored){}
        }
        cities.add(city);
        fitness -= time;
        this.time += time;
        logs.add(stringBuilder.toString());
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
//        for(int i = 0; i < cities.size() - 1; i++) {
////            sb.append(cities.get(i).getIndex()).append(" -> ").append(cities.get(i + 1).getIndex()).append(" ")
////                    .append(cities.get(i).distanceTo(cities.get(i + 1))).append("\n");
//            sb.append(cities.get(i).getIndex()).append(", ");
//        }
//        sb.append(cities.getLast().getIndex()).append("\n");
//        sb.append("Time: ").append(time).append("\n");
//        sb.append("Knapsack weight ").append(knapsack.getWeight()).append("\n");

        for (String log : logs) {
            sb.append(log).append("\n");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Solution other)) return false;
        return this.cities.equals(other.cities) && this.knapsack.equals(other.knapsack);
    }
}
