package resqnode.data;

import resqnode.model.CityGraph;
import resqnode.model.CityNode;
import resqnode.model.NodeState;

public class GraphLoader {
    public static CityGraph loadSampleGraph() {
        CityGraph graph = new CityGraph();
        graph.addNode(new CityNode(0, "Clock Tower", 400, 300, NodeState.BARRIER));
        graph.addNode(new CityNode(1, "Rajpur Road", 400, 150, NodeState.UNVISITED));
        graph.addNode(new CityNode(2, "ISBT", 300, 500, NodeState.BARRIER));
        graph.addNode(new CityNode(3, "Prem Nagar", 150, 250, NodeState.UNVISITED));
        graph.addNode(new CityNode(4, "Clement Town", 200, 450, NodeState.UNVISITED));
        graph.addNode(new CityNode(5, "Dalanwala", 500, 250, NodeState.UNVISITED));
        graph.addNode(new CityNode(6, "Vasant Vihar", 250, 350, NodeState.UNVISITED));
        graph.addNode(new CityNode(7, "Patel Nagar", 350, 400, NodeState.UNVISITED));
        graph.addNode(new CityNode(8, "Raipur", 600, 350, NodeState.UNVISITED));
        graph.addNode(new CityNode(9, "Sahastradhara", 650, 150, NodeState.SOURCE));
        graph.addNode(new CityNode(10, "GMS Road", 300, 300, NodeState.UNVISITED));
        graph.addNode(new CityNode(11, "Dharampur", 500, 400, NodeState.UNVISITED));
        graph.addNode(new CityNode(12, "Karanpur", 480, 200, NodeState.UNVISITED));
        graph.addNode(new CityNode(13, "Ballupur", 300, 200, NodeState.BARRIER));
        graph.addNode(new CityNode(14, "Garhi Cantt", 250, 100, NodeState.BARRIER));
        graph.addNode(new CityNode(15, "Majra", 250, 500, NodeState.UNVISITED));
        graph.addNode(new CityNode(16, "Niranjanpur", 350, 450, NodeState.UNVISITED));
        graph.addNode(new CityNode(17, "Jakhan", 450, 100, NodeState.UNVISITED));
        graph.addNode(new CityNode(18, "Rispana", 550, 450, NodeState.SOURCE));
        graph.addNode(new CityNode(19, "Bindal", 350, 200, NodeState.SOURCE));
        graph.addEdge(0, 1);
        graph.addEdge(0, 5);
        graph.addEdge(0, 7);
        graph.addEdge(0, 10);
        graph.addEdge(1, 12);
        graph.addEdge(1, 17);
        graph.addEdge(1, 19);
        graph.addEdge(2, 4);
        graph.addEdge(2, 15);
        graph.addEdge(2, 16);
        graph.addEdge(3, 13);
        graph.addEdge(3, 14);
        graph.addEdge(4, 15);
        graph.addEdge(5, 12);
        graph.addEdge(5, 11);
        graph.addEdge(6, 10);
        graph.addEdge(6, 13);
        graph.addEdge(7, 10);
        graph.addEdge(7, 16);
        graph.addEdge(8, 5);
        graph.addEdge(8, 11);
        graph.addEdge(8, 18);
        graph.addEdge(9, 17);
        graph.addEdge(9, 8);
        graph.addEdge(10, 13);
        graph.addEdge(11, 18);
        graph.addEdge(12, 17);
        graph.addEdge(13, 14);
        graph.addEdge(13, 19);
        graph.addEdge(14, 1);
        graph.addEdge(15, 6);
        graph.addEdge(16, 11);
        graph.addEdge(18, 11);
        graph.addEdge(19, 14);
        return graph;
    }
}