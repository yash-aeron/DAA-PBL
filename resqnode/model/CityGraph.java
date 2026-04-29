package resqnode.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityGraph {
    public static class Edge {
        public int targetId;
        public int weight;
        public Edge(int targetId, int weight) {
            this.targetId = targetId;
            this.weight = weight;
        }
    }
    public Map<Integer, CityNode> nodes = new HashMap<>();
    public Map<Integer, List<Edge>> adjList = new HashMap<>();
    public void addNode(CityNode node) {
        nodes.put(node.id, node);
        adjList.putIfAbsent(node.id, new ArrayList<>());
    }
    public void addEdge(int id1, int id2, int weight) {
        adjList.get(id1).add(new Edge(id2, weight));
        adjList.get(id2).add(new Edge(id1, weight));
    }
    public void addEdge(int id1, int id2) {
        CityNode n1 = nodes.get(id1);
        CityNode n2 = nodes.get(id2);
        int weight = 1;
        if (n1 != null && n2 != null) {
            weight = (int) Math.hypot(n1.x - n2.x, n1.y - n2.y);
        }
        addEdge(id1, id2, weight);
    }
}