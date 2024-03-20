public class Main {
    public static void main(String[] args) {
        Problem problem = Problem.setupInstance(
                "src/problems/berlin52_n51_uncorr-similar-weights_01.ttp",
//                "src/problems/simple4_n6_02.ttp",
//                new EvolutionaryAlgorithm(1000, 10,1000, 0.7, 0.05));
//                new GreedyAlgorithm());
                new SimulatedAnnealing(0.9999, 100000, 0.001, 1));
//                new RandomSearch(1000);

//        long startTime = System.nanoTime();
        Solution solution = problem.solve();
//        long endTime = System.nanoTime();

//        long duration = (endTime - startTime);
//        System.out.println("Execution time: " + duration / 1000000 + "ms\n");
        System.out.println(solution);
    }
}