package resqnode.engine;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import resqnode.model.CityGraph;
import resqnode.model.CityNode;
import resqnode.model.NodeState;
public class FloodPropagatorTest {
    @Test
    void testBasicFloodPropagation() {
        CityGraph graph = new CityGraph();
        graph.addNode(new CityNode(0, "N0", 0, 0, NodeState.SOURCE));
        graph.addNode(new CityNode(1, "N1", 10, 0, NodeState.UNVISITED));
        graph.addNode(new CityNode(2, "N2", 20, 0, NodeState.UNVISITED));
        graph.addNode(new CityNode(3, "N3", 30, 0, NodeState.UNVISITED));
        graph.addNode(new CityNode(4, "N4", 40, 0, NodeState.UNVISITED));
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        int[] distances = new int[5];
        FloodPropagator propagator = new FloodPropagator();
        propagator.runSimulation(graph, distances);
        assertEquals(NodeState.FLOODED, graph.nodes.get(1).state);
        assertEquals(NodeState.FLOODED, graph.nodes.get(2).state);
        assertEquals(NodeState.FLOODED, graph.nodes.get(3).state);
        assertEquals(NodeState.FLOODED, graph.nodes.get(4).state);
    }
    @Test
    void testBarrierBlocksFlood() {
        CityGraph graph = new CityGraph();
        graph.addNode(new CityNode(0, "N0", 0, 0, NodeState.SOURCE));
        graph.addNode(new CityNode(1, "N1", 10, 0, NodeState.UNVISITED));
        graph.addNode(new CityNode(2, "N2", 20, 0, NodeState.BARRIER));
        graph.addNode(new CityNode(3, "N3", 30, 0, NodeState.UNVISITED));
        graph.addNode(new CityNode(4, "N4", 40, 0, NodeState.UNVISITED));
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        int[] distances = new int[5];
        FloodPropagator propagator = new FloodPropagator();
        propagator.runSimulation(graph, distances);
        assertEquals(NodeState.FLOODED, graph.nodes.get(1).state);
        assertEquals(NodeState.SAFE, graph.nodes.get(3).state);
        assertEquals(NodeState.SAFE, graph.nodes.get(4).state);
    }
    @Test
    void testMultipleSourcesFlood() {
        CityGraph graph = new CityGraph();
        graph.addNode(new CityNode(0, "N0", 0, 0, NodeState.SOURCE));
        graph.addNode(new CityNode(1, "N1", 10, 0, NodeState.UNVISITED));
        graph.addNode(new CityNode(2, "N2", 20, 0, NodeState.UNVISITED));
        graph.addNode(new CityNode(3, "N3", 30, 0, NodeState.SOURCE));
        graph.addNode(new CityNode(4, "N4", 40, 0, NodeState.UNVISITED));
        graph.addNode(new CityNode(5, "N5", 50, 0, NodeState.UNVISITED));
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);
        int[] distances = new int[6];
        FloodPropagator propagator = new FloodPropagator();
        propagator.runSimulation(graph, distances);
        assertEquals(NodeState.FLOODED, graph.nodes.get(1).state);
        assertEquals(NodeState.FLOODED, graph.nodes.get(2).state);
        assertEquals(NodeState.FLOODED, graph.nodes.get(4).state);
        assertEquals(NodeState.FLOODED, graph.nodes.get(5).state);
    }
    @Test
    void testAllBarriersExceptSource() {
        CityGraph graph = new CityGraph();
        graph.addNode(new CityNode(0, "N0", 0, 0, NodeState.SOURCE));
        graph.addNode(new CityNode(1, "N1", 10, 0, NodeState.BARRIER));
        graph.addNode(new CityNode(2, "N2", 0, 10, NodeState.BARRIER));
        graph.addNode(new CityNode(3, "N3", 10, 10, NodeState.BARRIER));
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        int[] distances = new int[4];
        FloodPropagator propagator = new FloodPropagator();
        propagator.runSimulation(graph, distances);
        assertEquals(NodeState.BARRIER, graph.nodes.get(1).state);
        assertEquals(NodeState.BARRIER, graph.nodes.get(2).state);
        assertEquals(NodeState.BARRIER, graph.nodes.get(3).state);
    }
    @Test
    void testNoSourceGraph() {
        CityGraph graph = new CityGraph();
        graph.addNode(new CityNode(0, "N0", 0, 0, NodeState.UNVISITED));
        graph.addNode(new CityNode(1, "N1", 10, 0, NodeState.UNVISITED));
        graph.addNode(new CityNode(2, "N2", 20, 0, NodeState.UNVISITED));
        graph.addNode(new CityNode(3, "N3", 30, 0, NodeState.BARRIER));
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        int[] distances = new int[4];
        FloodPropagator propagator = new FloodPropagator();
        propagator.runSimulation(graph, distances);
        assertEquals(NodeState.SAFE, graph.nodes.get(0).state);
        assertEquals(NodeState.SAFE, graph.nodes.get(1).state);
        assertEquals(NodeState.SAFE, graph.nodes.get(2).state);
        assertEquals(NodeState.BARRIER, graph.nodes.get(3).state);
    }
    @Test
    void testDisconnectedGraph() {
        CityGraph graph = new CityGraph();
        graph.addNode(new CityNode(0, "N0", 0, 0, NodeState.SOURCE));
        graph.addNode(new CityNode(1, "N1", 10, 0, NodeState.UNVISITED));
        graph.addNode(new CityNode(2, "N2", 20, 0, NodeState.UNVISITED));
        graph.addNode(new CityNode(3, "N3", 30, 0, NodeState.UNVISITED));
        graph.addNode(new CityNode(4, "N4", 40, 0, NodeState.UNVISITED));
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(3, 4);
        int[] distances = new int[5];
        FloodPropagator propagator = new FloodPropagator();
        propagator.runSimulation(graph, distances);
        assertEquals(NodeState.FLOODED, graph.nodes.get(1).state);
        assertEquals(NodeState.FLOODED, graph.nodes.get(2).state);
        assertEquals(NodeState.SAFE, graph.nodes.get(3).state);
        assertEquals(NodeState.SAFE, graph.nodes.get(4).state);
    }
}
