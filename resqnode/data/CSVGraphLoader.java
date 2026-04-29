package resqnode.data;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import resqnode.model.CityGraph;
import resqnode.model.CityNode;
import resqnode.model.NodeState;

public class CSVGraphLoader {
    public static CityGraph loadFromCSV(String filePath) {
        CityGraph graph = new CityGraph();
        java.util.Map<String, Integer> addedEdges = new java.util.HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length < 5) {
                    continue;
                }
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                int x = Integer.parseInt(parts[2].trim());
                int y = Integer.parseInt(parts[3].trim());
                String stateStr = parts[4].trim().toUpperCase();
                NodeState state;
                switch (stateStr) {
                    case "SOURCE":
                        state = NodeState.SOURCE;
                        break;
                    case "BARRIER":
                        state = NodeState.BARRIER;
                        break;
                    case "SAFE":
                        state = NodeState.SAFE;
                        break;
                    default:
                        state = NodeState.UNVISITED;
                        break;
                }
                graph.addNode(new CityNode(id, name, x, y, state));
                for (int i = 5; i < parts.length; i++) {
                    String neighborStr = parts[i].trim();
                    if (!neighborStr.isEmpty()) {
                        String[] nParts = neighborStr.split(":");
                        int neighborId = Integer.parseInt(nParts[0]);
                        int weight = nParts.length > 1 ? Integer.parseInt(nParts[1]) : -1;
                        
                        String edgeKey = Math.min(id, neighborId) + "-" + Math.max(id, neighborId);
                        if (!addedEdges.containsKey(edgeKey)) {
                            addedEdges.put(edgeKey, weight);
                        }
                    }
                }
            }
            for (java.util.Map.Entry<String, Integer> entry : addedEdges.entrySet()) {
                String[] ids = entry.getKey().split("-");
                int id1 = Integer.parseInt(ids[0]);
                int id2 = Integer.parseInt(ids[1]);
                int weight = entry.getValue();
                if (graph.nodes.containsKey(id1) && graph.nodes.containsKey(id2)) {
                    if (weight != -1) {
                        graph.addEdge(id1, id2, weight);
                    } else {
                        graph.addEdge(id1, id2);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading CSV file: " + e.getMessage());
            System.err.println("Falling back to default graph.");
            return GraphLoader.loadSampleGraph();
        } catch (NumberFormatException e) {
            System.err.println("Parse error in CSV file: " + e.getMessage());
            System.err.println("Falling back to default graph.");
            return GraphLoader.loadSampleGraph();
        }
        if (graph.nodes.isEmpty()) {
            System.err.println("CSV file produced empty graph. Falling back to default graph.");
            return GraphLoader.loadSampleGraph();
        }
        return graph;
    }
}
