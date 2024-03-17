public class Main {
    public static void main(String[] args) {
        Problem problem = new Problem(
                "src/problems/berlin52_n51_bounded-strongly-corr_01.ttp",
                new GreedyAlgorithm());

        problem.solve();
    }
}