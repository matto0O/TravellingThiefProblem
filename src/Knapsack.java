//import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Knapsack {
    private final int capacity;
    private int weight;
    private final ArrayList<Item> items;

    public Knapsack(int capacity) {
        this.capacity = capacity;
        items = new ArrayList<>();
        weight = 0;
    }

    public Knapsack copy() {
        Knapsack copy = new Knapsack(capacity);
        copy.weight = weight;
        copy.items.addAll(items);
        return copy;
    }

    public void putItem(Item item) {
        if (weight + item.getWeight() > capacity) {
            throw new IllegalArgumentException(item + " is too heavy for this knapsack (" + (capacity - weight) + ")");
        }
        weight += item.getWeight();
        items.add(item);
    }

    public int getWeight() {
        return weight;
    }

    public int getCapacity() {
        return capacity;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Knapsack other)) return false;
        return this.getItems().equals(other.getItems());
    }
}
