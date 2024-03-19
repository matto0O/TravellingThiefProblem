import java.util.Comparator;

public class FitnessComparator implements Comparator<Solution> {
    @Override
    public int compare(Solution o1, Solution o2) {
        return Double.compare(o1.getFitness(), o2.getFitness());
    }
}
