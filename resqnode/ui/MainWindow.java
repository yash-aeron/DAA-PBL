package resqnode.ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import resqnode.data.CSVGraphLoader;
import resqnode.data.GraphLoader;
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
    public JButton resetBtn;
    public JButton toggleBarrierBtn;
    public JButton loadCsvBtn;
    public int[] distances;
    public MainWindow() {
        this.setTitle("ResQ-Node: Flood Impact Area Detection System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 700);
        this.setLayout(new BorderLayout());
        this.graph = GraphLoader.loadSampleGraph();
        this.distances = new int[getMaxNodeId(graph) + 1];
        this.renderer = new GraphRenderer(this.graph);
        this.renderer.setParentWindow(this);
        this.renderer.setPreferredSize(new Dimension(800, 600));
        this.add(this.renderer, BorderLayout.CENTER);
        this.sidePanel = new JPanel();
        this.sidePanel.setLayout(new GridLayout(12, 1, 10, 10));
        this.sidePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.sidePanel.setPreferredSize(new Dimension(220, 700));
        this.totalNodesLbl = new JLabel("Total Nodes: " + this.graph.nodes.size());
        this.floodedLbl = new JLabel("Flooded: -");
        this.floodedLbl.setForeground(new Color(200, 50, 50));
        this.safeLbl = new JLabel("Safe: -");
        this.safeLbl.setForeground(new Color(34, 139, 34));
        this.barriersLbl = new JLabel("Barriers: -");
        this.barriersLbl.setForeground(new Color(120, 120, 120));
        this.runtimeLbl = new JLabel("Dijkstra Runtime (ms): -");
        this.percentLbl = new JLabel("% Area Flooded: -");
        this.runBtn = new JButton("Run Simulation");
        this.runBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runSimulationAction();
            }
        });
        this.resetBtn = new JButton("Reset");
        this.resetBtn.setFont(this.runBtn.getFont());
        this.resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetAction();
            }
        });
        this.toggleBarrierBtn = new JButton("Toggle Barrier");
        this.toggleBarrierBtn.setFont(this.runBtn.getFont());
        this.toggleBarrierBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleBarrierAction();
            }
        });
        this.loadCsvBtn = new JButton("Load CSV");
        this.loadCsvBtn.setFont(this.runBtn.getFont());
        this.loadCsvBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadCsvAction();
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
        this.sidePanel.add(this.resetBtn);
        this.sidePanel.add(this.toggleBarrierBtn);
        this.sidePanel.add(this.loadCsvBtn);
        this.sidePanel.add(new JLabel(""));
        this.add(this.sidePanel, BorderLayout.EAST);
    }
    public void runSimulationAction() {
        FloodPropagator propagator = new FloodPropagator();
        distances = new int[getMaxNodeId(graph) + 1];
        long timeMs = propagator.runSimulation(graph, distances);
        ZoneClassifier classifier = new ZoneClassifier();
        SimulationResult res = classifier.classify(graph, distances, timeMs);
        totalNodesLbl.setText("Total Nodes: " + res.totalNodes);
        floodedLbl.setText("Flooded: " + res.floodedCount);
        safeLbl.setText("Safe: " + res.safeCount);
        barriersLbl.setText("Barriers: " + res.barrierCount);
        runtimeLbl.setText("Dijkstra Runtime (ms): " + res.runtimeMs);
        percentLbl.setText(String.format("%% Area Flooded: %.1f%%", res.percentFlooded));
        renderer.setDistances(distances);
        renderer.setSimulationRan(true);
        renderer.clearEvacuationPath();
        renderer.repaint();
    }
    private void resetAction() {
        this.graph = GraphLoader.loadSampleGraph();
        this.distances = new int[getMaxNodeId(graph) + 1];
        this.renderer.graph = this.graph;
        this.renderer.setSimulationRan(false);
        this.renderer.setDistances(null);
        this.renderer.clearEvacuationPath();
        this.renderer.setBarrierMode(false);
        this.toggleBarrierBtn.setText("Toggle Barrier");
        this.totalNodesLbl.setText("Total Nodes: " + this.graph.nodes.size());
        this.floodedLbl.setText("Flooded: -");
        this.safeLbl.setText("Safe: -");
        this.barriersLbl.setText("Barriers: -");
        this.runtimeLbl.setText("Dijkstra Runtime (ms): -");
        this.percentLbl.setText("% Area Flooded: -");
        this.renderer.repaint();
    }
    private void toggleBarrierAction() {
        boolean current = renderer.isBarrierMode();
        renderer.setBarrierMode(!current);
        if (!current) {
            toggleBarrierBtn.setText("Exit Barrier Mode");
        } 
        else {
            toggleBarrierBtn.setText("Toggle Barrier");
        }
    }
    private void loadCsvAction() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
        chooser.setFileFilter(filter);
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            this.graph = CSVGraphLoader.loadFromCSV(path);
            this.renderer.graph = this.graph;
            this.distances = new int[getMaxNodeId(graph) + 1];
            this.renderer.setSimulationRan(false);
            this.renderer.setDistances(null);
            this.renderer.clearEvacuationPath();
            this.renderer.setBarrierMode(false);
            this.toggleBarrierBtn.setText("Toggle Barrier");
            this.totalNodesLbl.setText("Total Nodes: " + this.graph.nodes.size());
            this.floodedLbl.setText("Flooded: -");
            this.safeLbl.setText("Safe: -");
            this.barriersLbl.setText("Barriers: -");
            this.runtimeLbl.setText("Dijkstra Runtime (ms): -");
            this.percentLbl.setText("% Area Flooded: -");
            this.renderer.repaint();
        }
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
    private static int getMaxNodeId(CityGraph graph) {
        int maxId = 0;
        for (int id : graph.nodes.keySet()) {
            if (id > maxId) maxId = id;
        }
        return maxId;
    }
}