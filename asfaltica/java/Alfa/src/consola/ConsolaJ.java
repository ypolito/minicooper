package consola;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSeparator;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.Timer;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;


public class ConsolaJ extends JFrame {

	/** Time series for total memory used. */
	private TimeSeries total;
	/** Time series for free memory. */
	private TimeSeries free;
	
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConsolaJ frame = new ConsolaJ();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ConsolaJ() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		setContentPane(contentPane);
		
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		
		// Creacion del dataset
		TimeSeriesCollection dataset = crearDataset (30000);
		
		// Creacion del grafico
		JFreeChart chart = crearChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		
		// Integracion
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(toolBar, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(textPane, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(chartPanel, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chartPanel, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textPane, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE))
		);
		
		JButton btnIniciar = new JButton("Iniciar");
		toolBar.add(btnIniciar);
		
		JButton btnDetener = new JButton("Detener");
		toolBar.add(btnDetener);
		
		this.new DataGenerator(100).start();
		
		contentPane.setLayout(gl_contentPane);

	}
	
	/**
	* The data generator.
	*/
	class DataGenerator extends Timer implements ActionListener {
	/**
	* Constructor.
	*
	* @param interval the interval (in milliseconds)
	*/
	DataGenerator(int interval) {
	super(interval, null);
	addActionListener(this);
	}
	/**
	* Adds a new free/total memory reading to the dataset.
	*
	* @param event the action event.
	*/
	public void actionPerformed(ActionEvent event) {
	long f = Runtime.getRuntime().freeMemory();
	long t = Runtime.getRuntime().totalMemory();
	addTotalObservation(t);
	addFreeObservation(f);
	}
	}

	/**
	* Adds an observation to the ’free memory’ time series.
	*
	* @param y the free memory.
	*/
	private void addFreeObservation(double y) {
	this.free.add(new Millisecond(), y);
	}

	/**
	* Adds an observation to the ’total memory’ time series.
	*
	* @param y the total memory used.
	*/
	private void addTotalObservation(double y) {
	this.total.add(new Millisecond(), y);
	}

	/**
	* Crea el dataset.
	*
	* @param maxAge.
	*
	* @return a TimeSeriesCollection.
	*/	
	private TimeSeriesCollection crearDataset (int maxAge) {
		this.total = new TimeSeries("Total Memory", Millisecond.class);
		this.total.setMaximumItemAge(maxAge);
		this.free = new TimeSeries("Free Memory", Millisecond.class);
		this.free.setMaximumItemAge(maxAge);
		
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(this.total);
		dataset.addSeries(this.free);
		
		return dataset;
	
		
	};
	
	/**
	* Creates a chart.
	*
	* @param dataset the data for the chart.
	*
	* @return a chart.
	*/
	private static JFreeChart crearChart(TimeSeriesCollection dataset) {
		
		DateAxis domain = new DateAxis("Time");
		NumberAxis range = new NumberAxis("Memory");
		domain.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		range.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		domain.setLabelFont(new Font("SansSerif", Font.PLAIN, 14));
		range.setLabelFont(new Font("SansSerif", Font.PLAIN, 14));
		XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
		renderer.setSeriesPaint(0, Color.red);
		renderer.setSeriesPaint(1, Color.green);
		renderer.setStroke(new BasicStroke(3f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));

		XYPlot plot = new XYPlot(dataset, domain, range, renderer);
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		domain.setAutoRange(true);
		domain.setLowerMargin(0.0);
		domain.setUpperMargin(0.0);
		domain.setTickLabelsVisible(true);
		range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		JFreeChart chart = new JFreeChart("JVM Memory Usage",
		new Font("SansSerif", Font.BOLD, 24), plot, true);
		return chart;
	};
	
}
