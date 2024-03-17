public class Main {
    public static void main(String[] args) {
        Problem problem = new Problem(
                "src/problems/berlin52_n51_uncorr_01.ttp",
                new GreedyAlgorithm());

        long startTime = System.nanoTime();
        problem.solve();
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println(duration / 1000000 + "ms");
    }
}