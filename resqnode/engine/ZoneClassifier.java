package resqnode.engine;
import resqnode.model.CityGraph;
import resqnode.model.CityNode;
import resqnode.model.NodeState;
import resqnode.model.SimulationResult;

public class ZoneClassifier {
    public SimulationResult classify(CityGraph graph, int[] distances, long runtimeMs) {
        SimulationResult result = new SimulationResult();
        result.runtimeMs = runtimeMs;
        result.totalNodes = graph.nodes.size();
        int totalDist = 0;
        int distCount = 0;
        for (CityNode node : graph.nodes.values()) {
            if (node.state == NodeState.FLOODED) {
                result.floodedCount++;
                totalDist += distances[node.id];
                distCount++;
            }
            else if (node.state == NodeState.SAFE) {
                result.safeCount++;
            }
            else if (node.state == NodeState.BARRIER) {
                result.barrierCount++;
            }
        }
        if (distCount > 0) {
            result.avgDistance = (double) totalDist / distCount;
        }
        else {
            result.avgDistance = 0.0;
        }
        result.percentFlooded = ((double) result.floodedCount / result.totalNodes) * 100.0;
        return result;
    }
}