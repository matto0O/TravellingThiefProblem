public class Main {
    public static void main(String[] args) {
        Problem problem = new Problem(
                "src/problems/simple4_n6_02.ttp",
                new RandomSearch());

        long startTime = System.nanoTime();
        problem.solve();
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println(duration / 1000000 + "ms");
    }
}