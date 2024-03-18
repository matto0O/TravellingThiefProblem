public interface Optimizer {
    Solution solve(City[] cities, int knapsackSize, double minSpeed,
                   double maxSpeed, double[][] distanceMatrix, int iterations);
}
