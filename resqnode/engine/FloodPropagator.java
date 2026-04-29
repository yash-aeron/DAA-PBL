package resqnode.engine;
import java.util.Comparator;
import java.util.PriorityQueue;
import resqnode.model.CityGraph;
import resqnode.model.CityNode;
import resqnode.model.NodeState;

public class FloodPropagator {
    public long runSimulation(CityGraph graph, int[] distances) {
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < distances.length; i++) {
            distances[i] = Integer.MAX_VALUE;
        }
        
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        
        for (CityNode node : graph.nodes.values()) {
            if (node.state == NodeState.SOURCE) {
                pq.add(new int[]{node.id, 0});
                distances[node.id] = 0;
            }
        }

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int currentId = current[0];
            int currentDist = current[1];
            
            if (currentDist > distances[currentId]) continue;

            for (CityGraph.Edge edge : graph.adjList.get(currentId)) {
                int neighborId = edge.targetId;
                CityNode neighbor = graph.nodes.get(neighborId);
                
                if (neighbor.state != NodeState.BARRIER && neighbor.state != NodeState.SOURCE && neighbor.state != NodeState.SAFE) {
                    int newDist = currentDist + edge.weight;
                    if (newDist < distances[neighborId]) {
                        distances[neighborId] = newDist;
                        neighbor.state = NodeState.FLOODED;
                        pq.add(new int[]{neighborId, newDist});
                    }
                }
            }
        }
        
        for (CityNode node : graph.nodes.values()) {
            if (distances[node.id] == Integer.MAX_VALUE) {
                distances[node.id] = -1;
                if (node.state != NodeState.SOURCE && node.state != NodeState.BARRIER) {
                    node.state = NodeState.SAFE;
                }
            }
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}