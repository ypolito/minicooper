package consola;

import bluetooth.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.bluetooth.RemoteDevice;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

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

public class Stiffness extends JFrame {

	private JPanel contentPane;
	private JTextArea textArea = new JTextArea(6, 4);

	// Series para alimentar el dataset
	private XYSeries series1 = new XYSeries("Deformacion");

	ThreadIniciar iniciar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Stiffness frame = new Stiffness();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Crea el frame.
	 */
	public Stiffness() {
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
		JButton btnIniciar = new JButton("Iniciar");
		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (iniciar == null) {
					iniciar = new ThreadIniciar("ComBT");
					iniciar.start();
				} else {
					if (iniciar.isAlive() != true) {
						iniciar = new ThreadIniciar("ComBT");
						iniciar.start();
					} else {
						agregarLog("Proceso en ejecucion....");
					}

				}

			}
		});

		toolBar.add(btnIniciar);
		JButton btnDetener = new JButton("Detener");
		btnDetener.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (iniciar == null) {
				} else {
					if (iniciar.isAlive() == true) {
						iniciar.stop();
						agregarLog("Se DETIENE el aplicativo");
					}

				}

			}
		});

		toolBar.add(btnDetener);

		// Creacion del grafico
		XYDataset dataset = createDataset();
		JFreeChart chart = createChart(dataset);

		// Integracion del grafico
		ChartPanel chartPanel = new ChartPanel(chart);
		getContentPane().add(chartPanel, BorderLayout.CENTER);

		// Panel de Log
		JScrollPane scroll = new JScrollPane(textArea);
		textArea.setEditable(false);
		contentPane.add(scroll, BorderLayout.SOUTH);
		agregarLog("Aplicativo disponible ...");

	}

	/**
	 * Registra informacion en el log.
	 */
	private void agregarLog(String text) {
		textArea.append(text + "\r\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

	/**
	 * Creates a chart.
	 * 
	 * @param dataset
	 *            the data for the chart.
	 * 
	 * @return a chart.
	 */
	private static JFreeChart createChart(XYDataset dataset) {

		// create the chart...
		JFreeChart chart = ChartFactory.createXYLineChart(
				"Deformacion y carga", // chart title
				"X", // x axis label
				"Y", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);
		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		// RGBtoHSB(240, 240, 240, hsbvals)

		chart.setBackgroundPaint(Color.getHSBColor(0, 0, 0.94f));
		// chart.setBackgroundPaint(Color.blue);
		// get a reference to the plot for further customisation...
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));

		plot.setDomainGridlinePaint(Color.black);
		plot.setRangeGridlinePaint(Color.black);
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot
				.getRenderer();

		renderer.setShapesVisible(true);
		renderer.setShapesFilled(false);
		// change the auto tick unit selection to integer units only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// OPTIONAL CUSTOMISATION COMPLETED.
		return chart;
	}

	/**
	 * Inicia el dataset.
	 * 
	 * @return a sample dataset.
	 */
	private XYDataset createDataset() {
		this.series1.add(0.0, 0.0);
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
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
	
	
	private class ThreadIniciar extends Thread {

		/**
		 * Inicia el registro de las mediciones de arduino
		 * 
		 * @param args
		 */
		public ThreadIniciar(String arg) {
			// TODO Auto-generated method stub
			super(arg);

		}

		// Redefinicion del metodo run()
		public void run() {
			agregarLog("Inicia la busqueda BT...");
			try {
				Coneccion conBT = new Coneccion();
				conBT.buscarDispositivos();
				if (conBT.vecDevices.size() > 0) {
					agregarLog("Se detectaron dispositivos BT...");
					// print bluetooth device addresses and names in the format
					// [ No. address (name) ]
					for (int i = 0; i < conBT.vecDevices.size(); i++) {
						agregarLog((i+1)+". "+conBT.vecDevicesNames.elementAt(i));
					}

					agregarLog("Finalizo busqueda BT");
					
					for (int j = 0; j < 10; j++)
					{
					if (conBT.connectionURL == null) {
					  agregarLog("Conectar al servicio(1)... intento "+j);
					  conBT.buscarServicio(1);
					}
					if (conBT.connectionURL == null) {
						agregarLog("El dispositivo no soporta el servicio Simple SPP.");
						
					}else
					{
						conBT.abrirConeccion();
						if (conBT.streamConnection != null)
						{
							agregarLog("Enviar dato...");
							conBT.enviarDato("SF+1");
							
							for (int i = 0; i < 100; i++) {
							
							String dato = conBT.recibirDato();
							if (dato != null)
							{
							  agregarLog("Se recibio un dato");
							  addDatasetValue ();
							}
							}
						}
					}
					}

				}
				else
				{
					agregarLog("No se detectaron dispositivos BT.");
				}

				agregarLog("Fin coneccion BT.");
				// appendText(j + " Esta linea se añadirá al final del texto.");
				// textPane.setText(textPane.getText()+"\nEsta linea se añadirá al final del texto."+j
				// );

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
