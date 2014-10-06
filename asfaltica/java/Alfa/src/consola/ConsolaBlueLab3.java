package consola;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.BoxLayout;
import javax.swing.JTextPane;

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


import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JLabel;
import javax.swing.Timer;



public class ConsolaBlueLab3 extends JFrame {

	private JPanel contentPane;
	private JTextPane textPane;
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
					ConsolaBlueLab3 frame = new ConsolaBlueLab3();
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
	public ConsolaBlueLab3() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		//contentPane.add(textPane, BorderLayout.SOUTH);
		contentPane.add((new JScrollPane(textPane)), BorderLayout.SOUTH);
		
		
		// Creacion del grafico
		XYDataset dataset = createDataset();
		JFreeChart chart = createChart(dataset);
		// Integracion
		ChartPanel chartPanel = new ChartPanel(chart);
		getContentPane().add(chartPanel, BorderLayout.CENTER);
		
		// Creacion del toolbar				
		JToolBar toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
				
		JButton btnNewButton = new JButton("Iniciar");
		toolBar.add(btnNewButton);
				
		JButton btnNewButton_1 = new JButton("Detener");
		toolBar.add(btnNewButton_1);
		
		textPane.setText(textPane.getText()+"\nLogging.....");
		SimpleThread st = new SimpleThread("log");
		st.start();
		// minimoServidor ms = new minimoServidor("server");
		// ms.start();
		// Inicia el servidor
		textPane.setText(textPane.getText()+"\nServidor Iniciado");
		try {
				cliente();
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			textPane.setText(textPane.getText()+"\nERROR en la conexion");
		}
		// Inicia el generador de datos
		Dg.start();
	
			
		
		
				
	}
	
	/**
	* Creates a sample dataset.
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
	
	public class SimpleThread extends Thread {

		/**
		 * @param args
		 */
		public SimpleThread(String arg) {
			// TODO Auto-generated method stub
			super(arg);

		}
		// redefinicion del metodo run()
		public void run() {
			int j = 0;
			try {
				while (1 == 1) {
				sleep(1000);
				j = j +1;
				appendText(j + " Esta linea se añadirá al final del texto.");
				//textPane.setText(textPane.getText()+"\nEsta linea se añadirá al final del texto."+j );
				
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Agrega texto al JPaneText.
	 * 
	 */
	public void  appendText(String text)  {
		try {
		 Document doc = textPane.getDocument();
		 // Move the insertion point to the end
       
		// Move the insertion point to the end
		 textPane.setCaretPosition(doc.getLength());
		// Insert the text
		 textPane.replaceSelection(text);
		 // Convert the new end location
	      // to view co-ordinates
	      Rectangle r = textPane.modelToView(doc.getLength());
	      
	      // Finally, scroll so that the new text is visible
	      if (r != null) {
	    	  textPane.scrollRectToVisible(r);
	      }
	      
	 } catch (BadLocationException e) {
	      System.out.println("Failed to append text: " + e);
	    }	
	}

	/**
	 * Lee texto de la coneccion tcp/ip.
	 * 
	 */
	
	public void cliente ()  throws IOException
	
	 {
	
	        int c;
	        Socket s;
	        InputStream sIn;

	        // Abrimos una conexión con depserver en el puerto 4321
	        s = new Socket( "localhost",4322 );

	        // Obtenemos un controlador de fichero de entrada del socket y
	        // leemos esa entrada
	        sIn = s.getInputStream();
	        while( ( c = sIn.read() ) != -1 )
	          //  System.out.print( (char)c );
	        textPane.setText(textPane.getText()+ (char)c);
	    
	        // Cuando se alcance el fin de fichero, cerramos la conexión y
	        // abandonamos
	        s.close();
	        }
	

	/**
	* Agrega valores al dataset.
	*
	*/
	private void addDatasetValue ()
	
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
	
	
}


