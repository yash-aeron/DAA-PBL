package resqnode.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityGraph {
    public Map<Integer, CityNode> nodes = new HashMap<>();
    public Map<Integer, List<Integer>> adjList = new HashMap<>();

    public void addNode(CityNode node) {
        nodes.put(node.id, node);
        adjList.putIfAbsent(node.id, new ArrayList<>());
    }

    public void addEdge(int id1, int id2) {
        adjList.get(id1).add(id2);
        adjList.get(id2).add(id1);
    }
}