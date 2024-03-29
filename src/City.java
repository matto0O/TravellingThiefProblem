//import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class City {
    private final int index;
    private final double coordinateX, coordinateY;
    private final ArrayList<Item> items;

    public City(int index, double coordinateX, double coordinateY) {
        this.index = index;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        items = new ArrayList<>();
    }

    public int getIndex() {
        return index;
    }

    public double getCoordinateX() {
        return coordinateX;
    }

    public double getCoordinateY() {
        return coordinateY;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<Item> getItemsLighterThan(int maxWeight) {
        ArrayList<Item> itemsLighterThan = new ArrayList<>();
        for (Item item : items) {
            if (item.getWeight() <= maxWeight) {
                itemsLighterThan.add(item);
            }
        }
        return itemsLighterThan;
    }

    public Item getMostValuableItem(int maxWeight){
        Item mostValuableItem = null;
        for (Item item : items) {
            if (item.getWeight() <= maxWeight) {
                if(mostValuableItem == null || mostValuableItem.getProfit() < item.getProfit()) {
                    mostValuableItem = item;
                }
            }
        }
        return mostValuableItem;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public double distanceTo(City city) {
        double xDistance = Math.abs(getCoordinateX() - city.getCoordinateX());
        double yDistance = Math.abs(getCoordinateY() - city.getCoordinateY());
        return Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));
    }

    @Override
    public String toString() {
        return "City nr " + index + " (" + coordinateX + ", " + coordinateY + "), itemCount = " + items.size();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof City)
            return this.index == ((City)obj).index;
        return false;
    }
}
