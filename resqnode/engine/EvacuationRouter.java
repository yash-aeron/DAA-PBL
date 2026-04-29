package resqnode.engine;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import resqnode.model.CityGraph;
import resqnode.model.CityNode;
import resqnode.model.NodeState;

public class EvacuationRouter {
    public List<Integer> findEvacuationPath(CityGraph graph, int startNodeId) {
        return dijkstraSearch(graph, startNodeId);
    }

    private List<Integer> dijkstraSearch(CityGraph graph, int startNodeId) {
        int maxId = 0;
        for (int id : graph.nodes.keySet()) {
            if (id > maxId) {
                maxId = id;
            }
        }
        int size = maxId + 1;
        int[] parent = new int[size];
        int[] distance = new int[size];
        
        for (int i = 0; i < size; i++) {
            parent[i] = -1;
            distance[i] = Integer.MAX_VALUE;
        }
        
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{startNodeId, 0});
        distance[startNodeId] = 0;
        
        int targetId = -1;
        
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int currentId = current[0];
            int currentDist = current[1];
            
            // Optimization: skip if we found a strictly better path already
            if (currentDist > distance[currentId]) continue;

            CityNode currentNode = graph.nodes.get(currentId);
            
            if (currentId != startNodeId && currentNode.state == NodeState.SAFE) {
                targetId = currentId;
                break;
            }
            
            for (CityGraph.Edge edge : graph.adjList.get(currentId)) {
                int neighborId = edge.targetId;
                CityNode neighbor = graph.nodes.get(neighborId);               
                
                // Never route towards sources of flood
                if (neighbor.state == NodeState.SOURCE) continue;
                
                int weight = edge.weight;
                if (neighbor.state == NodeState.BARRIER) {
                    weight += 10000;
                } else if (neighbor.state == NodeState.FLOODED) {
                    weight += 2000;
                }
                
                int newDist = currentDist + weight;
                if (newDist < distance[neighborId]) {
                    distance[neighborId] = newDist;
                    parent[neighborId] = currentId;
                    pq.add(new int[]{neighborId, newDist});
                }
            }
        }
        
        if (targetId == -1) {
            return new ArrayList<>();
        }
        List<Integer> path = new ArrayList<>();
        int current = targetId;
        while (current != -1) {
            path.add(current);
            current = parent[current];
        }
        Collections.reverse(path);
        return path;
    }
}
