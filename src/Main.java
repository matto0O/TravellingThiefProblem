public class Main {
    public static void main(String[] args) {
        Problem problem = new Problem(
//                "src/problems/berlin52_n51_uncorr-similar-weights_01.ttp",
                "src/problems/simple4_n6_02.ttp",
                new EvolutionaryAlgorithm(10, 3,
                        0.5, 0.05));

        long startTime = System.nanoTime();
        Solution solution = problem.solve();
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println("Execution time: " + duration / 1000000 + "ms\n");
        System.out.println(solution);
    }
}