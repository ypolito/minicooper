package consola;

import bluetooth.*;
import puertoCOM.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
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

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

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

import java.util.EventListener;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import java.awt.Canvas;
import java.awt.Panel;
import javax.swing.JLabel;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import javax.swing.ImageIcon;
import java.awt.GridLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;

public class Stiffness extends JFrame {

	private JPanel contentPane;
	private JTextArea textArea = new JTextArea(6, 4);
	private static JFreeChart chart;

	// Series para alimentar el dataset
	private XYSeries series1 = new XYSeries("Temperatura"); // Deformacion
	private XYSeries series2 = new XYSeries("Humedad");

	public static Stiffness frame;

	private puertoCOM.Coneccion conSERIE;
	private ThreadBT iniciarBT;
	private ThreadCOM iniciarCOM;

	private String dataProc;
	private String identificado = "N";

	private String puertoCOMBT;
	private String[] portNames;

	private final String tipoComunicacion = "COM"; // Valores posibles COM o BT.
													// Informa el tipo de
													// comunnicacion que
	private JToggleButton tglbtnExportar;
	/* Preferencias */

	//private ImageIcon img = new ImageIcon("G:\\MisDocumentos\\download examples\\Java\\Leds\\verde2.jpg");
	private ImageIcon img = new ImageIcon("G:\\MisDocumentos\\download examples\\Java\\Leds\\rojo.jpg");
	private JLabel lblNewJgoodiesLabel;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Stiffness();
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
		// Boton Iniciar
		JButton btnIniciar = new JButton("Iniciar");
		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				iniciar();
			}
		});
		
		lblNewJgoodiesLabel = DefaultComponentFactory.getInstance().createLabel("");
		toolBar.add(lblNewJgoodiesLabel);
		lblNewJgoodiesLabel.setIcon(new ImageIcon(img.getImage().getScaledInstance(32,32,Image.SCALE_SMOOTH)));
		
		Component rigidArea = Box.createRigidArea(new Dimension(10, 32));
		toolBar.add(rigidArea);

		toolBar.add(btnIniciar);
		// Boton Detener
		JButton btnDetener = new JButton("Detener");
		btnDetener.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				detener();

				// if (iniciarCOM != null) {
				//
				// if (iniciarCOM.isAlive() == true) {
				// try {
				// conSERIE.enviarDato("FIN");
				// conSERIE.cerrarConeccion();
				// agregarLog("Fin coneccion COM.");
				// } catch (SerialPortException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				//
				// /* se debe revisasr el stop porque esta depreciado */
				// iniciarCOM.stop();
				// // iniciarCOM.interrupt();
				// agregarLog("Se DETIENE el aplicativo");
				// }
				//
				// }

			}
		});

		toolBar.add(btnDetener);
		
		JButton btnPreferencias = new JButton("Preferencias");
		btnPreferencias.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Configuracion newWindow = new Configuracion();
				newWindow.setModal(true);
				newWindow.setConsola(frame);
				newWindow.setVisible(true);				
			}
		});		
		
		toolBar.add(btnPreferencias);
		
		//JToggleButton tglbtnExportar = new JToggleButton("Exportar");
		tglbtnExportar = new JToggleButton("Exportar");
		tglbtnExportar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//tglbtnExportar.setLabel("Descartar");
				if (tglbtnExportar.isSelected()) {
				tglbtnExportar.setText("Descartar");
				setLed("rojo");
				agregarLog("Descartar las mediciones.");}
				else {tglbtnExportar.setText("Exportar");
				setLed("verde");
				agregarLog("Almacenar las meidiciones.");
				}
			}
		});
		toolBar.add(tglbtnExportar);

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

	private void iniciar() {
		if (tipoComunicacion.compareTo("BT") == 0) {
			/* comunicacion BT */
			if (iniciarBT == null) {
				iniciarBT = new ThreadBT("ComBT");
				iniciarBT.start();
			} else {
				if (iniciarBT.isAlive() != true) {
					// agregarLog("iniciar.isAlive() != true...");
					iniciarBT = new ThreadBT("ComBT");
					// iniciar = new ThreadSerial("ComSERIE");
					iniciarBT.start();
				} else {
					agregarLog("Proceso en ejecucion....");
				}
			}
		} else {/* comunicacion SERIE */
			/* iniciarSerie(); */
			if (tipoComunicacion.compareTo("COM") == 0) {
				/* comunicacion BT */
				if (iniciarCOM == null) {
					agregarLog("iniciarCOM null");
					iniciarCOM = new ThreadCOM("ComBT");
					iniciarCOM.start();
				} else {
					if (iniciarCOM.isAlive() != true) {
						// agregarLog("iniciar.isAlive() != true...");
						iniciarCOM = new ThreadCOM("ComBT");
						iniciarCOM.start();
					} else {
						agregarLog("Proceso en ejecucion....");
					}
				}
			}

		}
	}

	private void detener() {

		/* prueba para limpiar el chart */

		if (tipoComunicacion.compareTo("BT1") == 0) {
			/* comunicacion BT */
			if (iniciarBT == null) {
			} else {
				if (iniciarBT.isAlive() != true) {
					// agregarLog("iniciar.isAlive() != true...");
					iniciarBT = new ThreadBT("ComBT");
					// iniciar = new ThreadSerial("ComSERIE");
					iniciarBT.start();
				} else {
					agregarLog("Proceso en ejecucion....");
				}
			}
		} else {/* comunicacion SERIE */
			/* iniciarSerie(); */
			if (tipoComunicacion.compareTo("COM1") == 0) {
				/* comunicacion BT */
				if (iniciarCOM == null) {
				} else {
					if (iniciarCOM.isAlive() != true) {
						// agregarLog("iniciar.isAlive() != true...");
						iniciarCOM = new ThreadCOM("ComBT");
						iniciarCOM.start();
					} else {
						agregarLog("Proceso en ejecucion....");
					}
				}
			}

		}
	}

	/**
	 * Registra informacion en el log.
	 */
	public void agregarLog(String text) {
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
		// JFreeChart
		chart = ChartFactory.createXYLineChart("Temperatura y Humedad", // chart
																		// title
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
		this.series2.add(0.0, 0.0);
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		return dataset;
	}

	/**
	 * Agrega valores al dataset.
	 * 
	 */
	public void addDatasetValue(double temp, double hum, double tiempo)

	{
		series1.add(tiempo, temp);
		series2.add(tiempo, hum);
		// series3.add(maxX3, 3.0);

	}

	
	public void clearChart(){
		
		/* Limpia la pantalla antes de inicar el proceso de medicion */
		chart.setNotify(false);
		series1.clear();
		series2.clear();
		series1.add(0.0, 0.0);
		series2.add(0.0, 0.0);
		chart.setNotify(true);

	}
	/**
	 * Procesa la informacino capturada en el puerto COM.
	 * 
	 */
	public void addDatasetPort(String receivedData) {
		if (dataProc == null) {
			dataProc = receivedData;
		} else {
			dataProc = dataProc.concat(receivedData);
		}
		// agregarLog("addDatasetPort "+dataProc+" "+dataProc.length());

		int inicio = dataProc.indexOf("<");
		int fin = dataProc.indexOf(">");
		if (inicio >= 0 && fin >= 0) {
			String dataAdd = dataProc.substring(inicio, fin + 1);
			// agregarLog(dataAdd);

			// Identificacion positiva
			if (dataAdd.compareTo("<id STIFNESS>") == 0) {
				identificado = "S";
				// agregarLog("IDENTIFICADO");
			}

			if (dataAdd.compareTo("<RETURN>") == 0)

			{
				try {
					iniciarCOM.liberaThread();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			//
			/* Obtener TEMPERTAURA, HUMEDAD y TIEMPO */
			int temp = dataAdd.indexOf("T:");
			int hum = dataAdd.indexOf("H:");
			int tiempo = dataAdd.indexOf("t:");

			if (temp >= 0 && hum >= 0 && tiempo >= 0) {
				Double tempVal = Double.parseDouble(dataAdd.substring(temp + 2,
						hum - 1));
				Double humVal = Double.parseDouble(dataAdd.substring(hum + 2,
						tiempo - 1));
				Double tiempoVal = Double.parseDouble(dataAdd.substring(
						tiempo + 2, fin));
				addDatasetValue(tempVal, humVal, tiempoVal);
			}
			// agregarLog("Substring "+inicio+" , "+fin+" ,datAdd "+dataAdd);
			// Se extrae la frase detectada
			if (dataProc != dataAdd) {
				dataProc = dataProc.replaceFirst(dataAdd, "");
			} else {
				dataProc = "";
			}

		}

		/*
		 * Procesamiento
		 * 
		 * Hasta que no se encuentre un par <> no se procesa el dato
		 */

	}

	private class ThreadBT extends Thread {

		/**
		 * Inicia el registro de las mediciones de arduino
		 * 
		 * @param args
		 */
		public ThreadBT(String arg) {
			// TODO Auto-generated method stub
			super(arg);

		}

		// Redefinicion del metodo run()
		public void run() {
			agregarLog("Inicia la busqueda BT...");
			try {
				bluetooth.Coneccion conBT = new bluetooth.Coneccion();
				conBT.buscarDispositivos();
				if (conBT.vecDevices.size() > 0) {
					agregarLog("Se detectaron dispositivos BT...");
					// print bluetooth device addresses and names in the format
					// [ No. address (name) ]
					for (int i = 0; i < conBT.vecDevices.size(); i++) {
						agregarLog((i + 1) + ". "
								+ conBT.vecDevicesNames.elementAt(i));
					}

					agregarLog("Finalizo busqueda BT");

					for (int j = 0; j < 10; j++) {
						if (conBT.connectionURL == null) {
							agregarLog("Conectar al servicio(1)... intento "
									+ j);
							conBT.buscarServicio(1);
						}
						if (conBT.connectionURL == null) {
							agregarLog("El dispositivo no soporta el servicio Simple SPP.");

						} else {
							conBT.abrirConeccion();
							if (conBT.streamConnection != null) {
								agregarLog("Enviar dato...");
								conBT.enviarDato("SF+1");

								for (int i = 0; i < 100; i++) {

									String dato = conBT.recibirDato();
									if (dato != null) {
										agregarLog("Se recibio un dato");
										addDatasetValue(0, 0, 0);
									}
								}
							}
						}
					}

				} else {
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

	private class ThreadCOM extends Thread {
		/**
		 * Inicia el registro de las mediciones de arduino. Se ejcuta como
		 * thread para utilizar informar los eventos en el area de log en el
		 * momento de de su aparicion y no de forma diferida.
		 * 
		 * @param args
		 */
		public ThreadCOM(String arg) {
			// TODO Auto-generated method stub
			super(arg);

		}

		// Redefinicion del metodo run()
		public void run() {
			comunicacion();

		}

		private synchronized void pausaThread() throws InterruptedException {
			wait();
		}

		// Called by Consumer
		public synchronized void liberaThread() throws InterruptedException {
			notify();
		}

		private void comunicacion() {

			if (identificado.compareTo("N") == 0) {
				// String puertoCOMBT = new String();
				agregarLog("Inicia la busqueda COM...");
				// String[] portNames =
				// puertoCOM.Coneccion.buscarDispositivos();}
				portNames = puertoCOM.Coneccion.buscarDispositivos();
				int i = 0;
				while (identificado.compareTo("N") == 0 && i < portNames.length) {

					// for(int i = 0; i < portNames.length; i++){

					// if(identificado.compareTo("N") == 0) {
					agregarLog(portNames[i]);
					try {
						puertoCOMBT = portNames[i];
						conSERIE = new puertoCOM.Coneccion(puertoCOMBT);
						conSERIE.setConsola(frame);
						try {
							conSERIE.abrirConeccion();
							conSERIE.enviarDato("IDT");
							conSERIE.recibirDato();

							/*
							 * Espera reloj 2 segundos para verificar la
							 * recepcion de algun dato
							 */
							Thread clock = new ThreadClock("reloj", 2000);
							clock.start();
							while (clock.isAlive() == true) {
							}
							if (identificado.compareTo("S") == 0) {
								puertoCOMBT = portNames[i];
							}

							else {
								conSERIE.cerrarConeccion();
							}
						}

						catch (Exception e) {
							agregarLog("Error al abrir el puerto "
									+ portNames[i]);
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					i++;
				}

				if (puertoCOMBT == null) {
					agregarLog("No se detecto ningun dispositivo BT");
				}

			}

			if (puertoCOMBT != null) {
				try {
					clearChart();
					conSERIE.enviarDato("INI");
					try {
						pausaThread();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block

						e.printStackTrace();
					}

				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	public class ThreadClock extends Thread {
		Integer pausa;

		// constructor. El parameotr str permite asignar un Nombre al thread
		public ThreadClock(String str, Integer miliseg) {
			super(str);
			pausa = miliseg;
		}

		// redefinición del método run()
		public void run() {

			try {
				Thread.sleep(pausa);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void setLed(String color) {
		if (color.compareTo("verde")== 0)
		{
			img = new ImageIcon("G:\\MisDocumentos\\download examples\\Java\\Leds\\verde2.jpg");
			
			

			
		}
		if (color.compareTo("amarillo")== 0)
		{}
		if (color.compareTo("rojo")== 0)
		{
			img = new ImageIcon("G:\\MisDocumentos\\download examples\\Java\\Leds\\rojo.jpg");
		}
		lblNewJgoodiesLabel.setIcon(new ImageIcon(img.getImage().getScaledInstance(32,32,Image.SCALE_SMOOTH)));
	}
	
}
