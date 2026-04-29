package resqnode.model;
//Node state machine
public enum NodeState {
    SOURCE,
    FLOODED,
    SAFE,
    BARRIER,
    UNVISITED
}