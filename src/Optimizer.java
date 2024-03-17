public interface Optimizer {
    Solution solve(City[] cities, Knapsack knapsack, double minSpeed,
                   double maxSpeed, double[][] distanceMatrix, int iterations);
}
