public class Item {
    private final int index;
    private final int profit;
    private final int weight;

    public Item(int index, int profit, int weight) {
        this.index = index;
        this.profit = profit;
        this.weight = weight;
    }

    public int getProfit() {
        return profit;
    }

    public int getWeight() {
        return weight;
    }

    private int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "Item of index" + index + ", profit=" + profit + ", weight=" + weight;
    }
}
