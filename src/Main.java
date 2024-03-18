public class Main {
    public static void main(String[] args) {
        Problem problem = new Problem(
                "src/problems/simple4_n6_02.ttp",
                new GreedyAlgorithm());

        long startTime = System.nanoTime();
        Solution solution = problem.solve();
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println("Execution time: " + duration / 1000000 + "ms\n");
        System.out.println(solution);
    }
}