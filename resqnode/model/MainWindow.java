package resqnode.ui;

import java.awt.BorderLayout;   //abstract window Toolkit
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;    //Java swing
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import resqnode.data.GraphLoader;     //import from source files
import resqnode.engine.FloodPropagator;
import resqnode.engine.ZoneClassifier;
import resqnode.model.CityGraph;
import resqnode.model.SimulationResult;

public class MainWindow extends JFrame {
    public CityGraph graph;
    public GraphRenderer renderer;
    public JPanel sidePanel;
    public JLabel totalNodesLbl;
    public JLabel floodedLbl;
    public JLabel safeLbl;
    public JLabel barriersLbl;
    public JLabel runtimeLbl;
    public JLabel percentLbl;
    public JButton runBtn;
    public int[] distances;

    public MainWindow() {
        this.setTitle("ResQ-Node: Flood Impact Area Detection System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 650);
        this.setLayout(new BorderLayout());
        this.graph = GraphLoader.loadSampleGraph();
        this.distances = new int[100];
        this.renderer = new GraphRenderer(this.graph);
        this.renderer.setPreferredSize(new Dimension(800, 600));
        this.add(this.renderer, BorderLayout.CENTER);
        this.sidePanel = new JPanel();
        this.sidePanel.setLayout(new GridLayout(8, 1, 10, 10));
        this.sidePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.sidePanel.setPreferredSize(new Dimension(200, 600));
        this.totalNodesLbl = new JLabel("Total Nodes: " + this.graph.nodes.size());
        this.floodedLbl = new JLabel("Flooded: 0");
        this.safeLbl = new JLabel("Safe: 0");
        this.barriersLbl = new JLabel("Barriers: 0");
        this.runtimeLbl = new JLabel("BFS Runtime (ms): 0");
        this.percentLbl = new JLabel("% Area Flooded: 0.0%");
        this.runBtn = new JButton("Run Simulation");
        this.runBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FloodPropagator propagator = new FloodPropagator();
                long timeMs = propagator.runSimulation(graph, distances);
                ZoneClassifier classifier = new ZoneClassifier();
                SimulationResult res = classifier.classify(graph, distances, timeMs);
                totalNodesLbl.setText("Total Nodes: " + res.totalNodes);
                floodedLbl.setText("Flooded: " + res.floodedCount);
                safeLbl.setText("Safe: " + res.safeCount);
                barriersLbl.setText("Barriers: " + res.barrierCount);
                runtimeLbl.setText("BFS Runtime (ms): " + res.runtimeMs);
                percentLbl.setText(String.format("%% Area Flooded: %.1f%%", res.percentFlooded));
                renderer.repaint();
            }
        });
        this.sidePanel.add(this.totalNodesLbl);
        this.sidePanel.add(this.floodedLbl);
        this.sidePanel.add(this.safeLbl);
        this.sidePanel.add(this.barriersLbl);
        this.sidePanel.add(this.runtimeLbl);
        this.sidePanel.add(this.percentLbl);
        this.sidePanel.add(new JLabel(""));
        this.sidePanel.add(this.runBtn);
        this.add(this.sidePanel, BorderLayout.EAST);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow window = new MainWindow();
                window.setLocationRelativeTo(null);
                window.setVisible(true);
            }
        });
    }
}
