public class SimulatedAnnealing implements Optimizer{

    // algorithm understanding based on the following source:
    // https://medium.com/@francis.allanah/travelling-salesman-problem-using-simulated-annealing-f547a71ab3c6
    // no code was copied from the source, only the algorithm understanding was based on it

    private final double coolingRate;
    private double temperature;
    private final double terminationTemperature;
    private final double mutationChance;

    public SimulatedAnnealing(double coolingRate, double startTemperature,
                              double terminationTemperature, double mutationChance){
        this.coolingRate = coolingRate;
        this.temperature = startTemperature;
        this.terminationTemperature = terminationTemperature;
        this.mutationChance = mutationChance;
    }

    private boolean acceptanceCriteria(double rivalFitness, double benchmarkFitness){
        double exp = Math.exp((rivalFitness - benchmarkFitness) / temperature);
        double random = Math.random();
        return exp >= random;
    }

    @Override
    public Solution solve() {
        RandomSearch rs = new RandomSearch(1);
        Solution benchmarkSolution = rs.solve();
        Solution rivalSolution = rs.solve();
        Solution bestSolution = benchmarkSolution;

        while(temperature > terminationTemperature){
            double benchmarkFitness = benchmarkSolution.getFitness();
            double rivalFitness = rivalSolution.getFitness();

            if(benchmarkFitness < rivalFitness){
                benchmarkSolution = rivalSolution;
                if (bestSolution.getFitness() < benchmarkFitness) bestSolution = benchmarkSolution;
                rivalSolution = Operators.mutation(benchmarkSolution, mutationChance);
            }
            else if (acceptanceCriteria(rivalFitness, benchmarkFitness)){
                benchmarkSolution = rivalSolution;
                rivalSolution = Operators.mutation(rivalSolution, mutationChance);
            }
            else{
                rivalSolution = rs.solve();
            }

            temperature *= coolingRate;
        }

        return bestSolution;
    }
}
