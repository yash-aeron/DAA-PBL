package resqnode.model;

public class CityNode {
    public int id;
    public String name;
    public int x;
    public int y;
    public NodeState state;

    public CityNode(int id, String name, int x, int y, NodeState state) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.state = state;
    }
}