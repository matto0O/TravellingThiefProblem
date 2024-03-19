import java.util.ArrayList;

public class Solution implements Comparable<Solution>{

    private Knapsack knapsack;
    private ArrayList<City> cities;
    private double fitness;
    private double time;

    public Solution(Knapsack knapsack, City startCity) {
        this.knapsack = knapsack;
        this.cities = new ArrayList<>();
        this.cities.add(startCity);
    }

    public double getFitness() {
        return fitness;
    }

    public ArrayList<City> getCities() {return cities;}

    public Knapsack getKnapsack() {return knapsack;}


    public void appendSolution(City city, double time, Item item) {
        if(item != null) {
            knapsack.putItem(item);
            fitness += item.getProfit();
        }
        cities.add(city);
        fitness -= time;
        this.time += time;
    }

    public Item itemStolenFromCity(City city){
        ArrayList<Item> itemsInCity = city.getItems();
        for(Item item: knapsack.getItems()){
            if(itemsInCity.contains(item))
                return item;
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
        sb.append("Cities: ");
        for(int i = 0; i < cities.size() - 1; i++) {
//            sb.append(cities.get(i).getIndex()).append(" -> ").append(cities.get(i + 1).getIndex()).append(" ")
//                    .append(cities.get(i).distanceTo(cities.get(i + 1))).append("\n");
            sb.append(cities.get(i).getIndex()).append(", ");
        }
        sb.append(cities.getLast().getIndex()).append("\n");
        sb.append("Time: ").append(time).append("\n");
        sb.append("Knapsack weight ").append(knapsack.getWeight()).append("\n");

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        Solution other = (Solution)obj;
        return this.cities.equals(other.cities) && this.knapsack.equals(other.knapsack);
    }
}
