package resqnode.engine;
import resqnode.model.CityGraph;
import resqnode.model.CityNode;
import resqnode.model.NodeState;
public class StressTest {
    public static CityGraph generateLinearGraph(int n) {
        CityGraph graph = new CityGraph();
        for (int i = 0; i < n; i++) {
            NodeState state = (i == 0) ? NodeState.SOURCE : NodeState.UNVISITED;
            graph.addNode(new CityNode(i, "Node" + i, i % 800, (i / 800) * 20, state));
        }
        for (int i = 0; i < n - 1; i++) {
            graph.addEdge(i, i + 1);
        }
        return graph;
    }
    public static void main(String[] args) {
        int[] sizes = {100, 500, 1000, 5000};
        long[] runtimes = new long[sizes.length];
        for (int s = 0; s < sizes.length; s++) {
            int n = sizes[s];
            CityGraph graph = generateLinearGraph(n);
            int[] distances = new int[n];
            FloodPropagator propagator = new FloodPropagator();
            long runtime = propagator.runSimulation(graph, distances);
            int floodedCount = 0;
            int safeCount = 0;
            for (CityNode node : graph.nodes.values()) {
                if (node.state == NodeState.FLOODED) {
                    floodedCount++;
                } else if (node.state == NodeState.SAFE) {
                    safeCount++;
                }
            }
            runtimes[s] = runtime;
            System.out.println("Nodes: " + n + " | Flooded: " + floodedCount + " | Safe: " + safeCount + " | Runtime: " + runtime + "ms");
        }
        if (runtimes[0] > 0 && runtimes[2] > runtimes[0] * 10) {
            System.out.println("WARNING: 1000-node run took more than 10x the 100-node run. Runtime may not be scaling linearly.");
        } else {
            System.out.println("Runtime scaling appears linear.");
        }
    }
}
