package resqnode.ui;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import resqnode.engine.EvacuationRouter;
import resqnode.model.CityGraph;
import resqnode.model.CityNode;
import resqnode.model.NodeState;

public class GraphRenderer extends JPanel {
    public CityGraph graph;
    private List<Integer> evacuationPath = new ArrayList<>();
    private boolean simulationRan = false;
    private int[] distances;
    private boolean barrierMode = false;
    private MainWindow parentWindow;
    public GraphRenderer(CityGraph graph) {
        this.graph = graph;
        this.setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
    }
    public void setParentWindow(MainWindow parentWindow) {
        this.parentWindow = parentWindow;
    }
    public void setEvacuationPath(List<Integer> path) {
        this.evacuationPath = path;
    }
    public void setSimulationRan(boolean ran) {
        this.simulationRan = ran;
    }
    public void setDistances(int[] distances) {
        this.distances = distances;
    }
    public void setBarrierMode(boolean mode) {
        this.barrierMode = mode;
    }
    public boolean isBarrierMode() {
        return this.barrierMode;
    }
    public void clearEvacuationPath() {
        this.evacuationPath = new ArrayList<>();
    }
    private void handleClick(int mx, int my) {
        CityNode nearest = null;
        double minDist = Double.MAX_VALUE;
        for (CityNode node : graph.nodes.values()) {
            double dist = Math.sqrt(Math.pow(node.x - mx, 2) + Math.pow(node.y - my, 2));
            if (dist < minDist) {
                minDist = dist;
                nearest = node;
            }
        }
        if (nearest == null || minDist > 24) {
            evacuationPath = new ArrayList<>();
            repaint();
            return;
        }
        if (barrierMode) {
            if (nearest.state == NodeState.BARRIER) {
                nearest.state = NodeState.UNVISITED;
            } 
            else if (nearest.state == NodeState.SAFE || nearest.state == NodeState.FLOODED || nearest.state == NodeState.UNVISITED) {
                nearest.state = NodeState.BARRIER;
            }
            if (parentWindow != null) {
                parentWindow.runSimulationAction();
            }
            return;
        }
        if (nearest.state == NodeState.FLOODED || nearest.state == NodeState.BARRIER) {
            EvacuationRouter router = new EvacuationRouter();
            List<Integer> path = router.findEvacuationPath(graph, nearest.id);
            this.evacuationPath = path;
        } 
        else {
            this.evacuationPath = new ArrayList<>();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.LIGHT_GRAY);
        for (Integer id : graph.nodes.keySet()) {
            CityNode nodeA = graph.nodes.get(id);
            for (CityGraph.Edge edge : graph.adjList.get(id)) {
                int neighborId = edge.targetId;
                if (id < neighborId) {
                    CityNode nodeB = graph.nodes.get(neighborId);
                    g2.drawLine(nodeA.x, nodeA.y, nodeB.x, nodeB.y);
                }
            }
        }
        for (CityNode node : graph.nodes.values()) {
            switch (node.state) {
                case SOURCE:
                    g2.setColor(Color.YELLOW);
                    break;
                case FLOODED:
                    g2.setColor(Color.RED);
                    break;
                case SAFE:
                    g2.setColor(Color.GREEN);
                    break;
                case BARRIER:
                    g2.setColor(Color.GRAY);
                    break;
                case UNVISITED:
                default:
                    g2.setColor(Color.BLUE);
                    break;
            }
            int r = 20;
            g2.fillOval(node.x - r, node.y - r, r * 2, r * 2);
            g2.setColor(Color.BLACK);
            g2.drawOval(node.x - r, node.y - r, r * 2, r * 2);
            FontMetrics fm = g2.getFontMetrics();
            int stringWidth = fm.stringWidth(node.name);
            g2.drawString(node.name, node.x - stringWidth / 2, node.y + r + 15);
        }
        if (simulationRan && distances != null) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            g2.setColor(Color.WHITE);
            for (CityNode node : graph.nodes.values()) {
                String label;
                if (node.state == NodeState.SOURCE) {
                    label = "S";
                } else if (node.state == NodeState.BARRIER) {
                    label = "X";
                } else if (node.id < distances.length && distances[node.id] >= 0) {
                    label = String.valueOf(distances[node.id]);
                } else {
                    label = "";
                }
                if (!label.isEmpty()) {
                    FontMetrics fm = g2.getFontMetrics();
                    int lw = fm.stringWidth(label);
                    int lh = fm.getAscent();
                    g2.drawString(label, node.x - lw / 2, node.y + lh / 2 - 1);
                }
            }
        }
        if (evacuationPath != null && evacuationPath.size() >= 2) {
            g2.setColor(new Color(0, 120, 255));
            g2.setStroke(new BasicStroke(4));
            for (int i = 0; i < evacuationPath.size() - 1; i++) {
                CityNode a = graph.nodes.get(evacuationPath.get(i));
                CityNode b = graph.nodes.get(evacuationPath.get(i + 1));
                g2.drawLine(a.x, a.y, b.x, b.y);
            }
            g2.setStroke(new BasicStroke(3));
            for (int nodeId : evacuationPath) {
                CityNode node = graph.nodes.get(nodeId);
                int r = 20;
                g2.drawOval(node.x - r, node.y - r, r * 2, r * 2);
            }
            g2.setStroke(new BasicStroke(1));
        }
    }
}