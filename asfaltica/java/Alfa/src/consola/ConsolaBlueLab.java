package consola;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;


import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;

public class ConsolaBlueLab extends JFrame {

	private JPanel contentPane;
	public JTextArea textArea = new JTextArea(6,4);	
	final ConsolaBlueLab esteFrame = this;
	SimpleThread miThread;
	
	// Series para alimentar el dataset
	XYSeries series1 = new XYSeries("First");
	// Timer para registrar los datos del arduino
	Timer Dg = new  DataGenerator(1000);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConsolaBlueLab frame = new ConsolaBlueLab();
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
	public ConsolaBlueLab() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		

		// Creacion del toolbar				
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		contentPane.add(toolBar, BorderLayout.NORTH);
		JButton btnNewButton = new JButton("Iniciar");
				btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Dg.isRunning() != true) {
/*					ConexionBlueLab client=new ConexionBlueLab();
		    		try {
		    			ConexionBlueLab.conexion(client, esteFrame);
		    			//ConexionBlueLab.conexion(client, );
		    		    //prueba();
		    			} catch (Exception e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    			}*/
					
    				if( miThread == null) {
   					 miThread = new SimpleThread("ComBT");
   					 miThread.start();	
    				}
    				else
    				{
        				if( miThread.isAlive() != true) {
          					 miThread = new SimpleThread("ComBT");
          					 miThread.start();	
           				}
    					
    				}
    				// Dg.start();
				}
			}
		});
		toolBar.add(btnNewButton);
		JButton btnNewButton_1 = new JButton("Detener");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Dg.isRunning() == true) {
    				Dg.stop();
    				
				}
			}
		});
		toolBar.add(btnNewButton_1);
		
		// Creacion del grafico
		XYDataset dataset = createDataset();
		JFreeChart chart = createChart(dataset);
		// Integracion
		ChartPanel chartPanel = new ChartPanel(chart);
		getContentPane().add(chartPanel, BorderLayout.CENTER);
		
		JScrollPane scroll = new JScrollPane (textArea);
		textArea.setEditable(false);
		contentPane.add(scroll, BorderLayout.SOUTH);
		textArea.setText("Aplicativo disponible...\r\n");
	}

	
	/**
	* Creates a chart.
	*
	* @param dataset the data for the chart.
	*
	* @return a chart.
	*/
	private static JFreeChart createChart(XYDataset dataset) {

	// create the chart...
	JFreeChart chart = ChartFactory.createXYLineChart(
	"Demo Grafico Lineas, integrado a JFreeChart", // chart title
	"X", // x axis label
	"Y", // y axis label
	dataset, // data
	PlotOrientation.VERTICAL,
	true, // include legend
	true, // tooltips
	false // urls
	);
	// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
	// RGBtoHSB(240, 240, 240, hsbvals)
	
	chart.setBackgroundPaint(Color.getHSBColor(0,0,0.94f));
	//chart.setBackgroundPaint(Color.blue);
	// get a reference to the plot for further customisation...
	XYPlot plot = (XYPlot) chart.getPlot();
	plot.setBackgroundPaint(Color.white);
	plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));


	plot.setDomainGridlinePaint(Color.black);
	plot.setRangeGridlinePaint(Color.black);
	XYLineAndShapeRenderer renderer
	= (XYLineAndShapeRenderer) plot.getRenderer();

	renderer.setShapesVisible(true);
	renderer.setShapesFilled(false);
	// change the auto tick unit selection to integer units only...
	NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	// OPTIONAL CUSTOMISATION COMPLETED.
	return chart;
	}
	
	
	/**
	* Inicializa el dataset.
	*
	* @return a sample dataset.
	*/
	private XYDataset createDataset() {
	//XYSeries series1 = new XYSeries("First");
	this.series1.add(0.0, 0.0);
	/*series1.add(2.0, 4.0);
	series1.add(3.0, 3.0);
	series1.add(4.0, 5.0);
	series1.add(5.0, 5.0);
	series1.add(6.0, 7.0);
	series1.add(7.0, 7.0);
	series1.add(8.0, 8.0);*/
	/*
	XYSeries series2 = new XYSeries("Second");
	series2.add(1.0, 5.0);
	series2.add(2.0, 7.0);
	series2.add(3.0, 6.0);
	series2.add(4.0, 8.0);
	series2.add(5.0, 4.0);
	series2.add(6.0, 4.0);
	series2.add(7.0, 2.0);
	series2.add(8.0, 1.0);
	XYSeries series3 = new XYSeries("Third");
	series3.add(3.0, 4.0);
	series3.add(4.0, 3.0);
	series3.add(5.0, 2.0);
	series3.add(6.0, 3.0);
	series3.add(7.0, 6.0);
	series3.add(8.0, 3.0);
	series3.add(9.0, 4.0);
	series3.add(10.0, 3.0);*/
	XYSeriesCollection dataset = new XYSeriesCollection();
	dataset.addSeries(series1);
/*	dataset.addSeries(series2);
	dataset.addSeries(series3);*/
	
	return dataset;
	}
	
	
	/**
	* Agrega valores al dataset.
	*
	*/
    void addDatasetValue ()
	
	{
		double maxX1 = series1.getMaxX() ;
		//double maxX2 = series2.getMaxX() ;
		//double maxX3 = series3.getMaxX() ;
		
		maxX1 = maxX1 +1.4;
		//maxX2 = maxX2 +1;
		//maxX3 = maxX3 +1;
		
		series1.add(maxX1, 3.0);
		//series2.add(maxX2, 3.0);
		//series3.add(maxX3, 3.0);
	
	}
	

	/**
	* Timer para incorporar lecturas al dataset.
	*
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
			 addDatasetValue();
			 }
		 }	
	
	
	////////////////
	
	public class SimpleThread extends Thread {
        // constructor. El parametro str permite asignar un Nombre al thread
        public SimpleThread (String str) {
        super(str);
        }

        // redefinición del método run()
        public void run() {
    		ConexionBlueLab client=new ConexionBlueLab();
    		try {
    			ConexionBlueLab.conexion(client, esteFrame);
    			} catch (Exception e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    			}
    		textArea.append("BT finalizado ...."+"\r\n");
        }
      }
	
	
	//////////////

	
	
	
	
	
	public void BTstart ()
	{
		
		miThread.start();
		
	}
	
	
	
}
