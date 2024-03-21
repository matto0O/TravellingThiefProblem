import java.util.ArrayList;
import java.util.HashMap;

public interface Optimizer {
    Solution solve();

    HashMap<String, Number> params();

    Number[] runSummary(int runNumber);

    boolean saveToFile(String fileName, int runNumber);

    String iterationDetails();

    String iterationPreview();

    Double[][] iterationNumbersPreview();
}
