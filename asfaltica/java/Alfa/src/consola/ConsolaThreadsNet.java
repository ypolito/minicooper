package consola;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.EventQueue;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;



import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.net.*;
import java.io.*;


public class ConsolaThreadsNet extends JFrame {

	private JPanel contentPane;
    private JTextArea textArea;
    private SimpleThread miThread;
	static  boolean estadoServicio = false;
	
	
	// Series para alimentar el dataset
	XYSeries series1 = new XYSeries("Humedad");
	XYSeries series2 = new XYSeries("Temperatura");
    
	ServerSocket s = null;
    Socket cliente = null;

    DataInputStream sIn; // Stream de entrada
    PrintStream sOut;    // Stream de salida

    String texto;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConsolaThreadsNet frame = new ConsolaThreadsNet();
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
	public ConsolaThreadsNet() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 512, 357);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		
        // Creacion del dataset
		XYDataset dataset = createDataset();
		
		// Creacion del grafico
		JFreeChart chart = createChart(dataset);		
		ChartPanel chartPanel = new ChartPanel(chart);
		
		// Integracion
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(toolBar, GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
				.addComponent(chartPanel, GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chartPanel, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))
		);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.append("Consola"+ "\n");
		
		
		JButton btnIniciar = new JButton("Iniciar");
		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// El contenido de la variable se actualiza con el nuevo thread, y se pierde control del thread anterior
				if (estadoServicio == false) {
				estadoServicio=true;					
				miThread = new SimpleThread("ComTC/IP");
				logeo("Se inicia thread");
				miThread.start();	
				}
			}
		});
		toolBar.add(btnIniciar);
		
		JButton btnDetener = new JButton("Detener");
		btnDetener.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (estadoServicio==true) {
					estadoServicio = false;
				logeo("Se detiene el thread");
				}
			}
		});
		toolBar.add(btnDetener);
		contentPane.setLayout(gl_contentPane);
	}

	
	public void logeo(String logtext) {
		textArea.append(logtext + "\n");
	}
	
	public class SimpleThread extends Thread {
        // constructor. El parametro str permite asignar un Nombre al thread
        public SimpleThread (String str) {
        super(str);
        }

        // redefinición del método run()
        public void run() {

        	int tiempo;
        	float humedad;
        	float temperatura;
        	int pos1;
        	int pos2;
        	
        	// Abrimos una conexión a la consolaJ en el puerto 9999
            // No podemos elegir un puerto por debajo del 1023 si no somos
            // usuarios con los máximos privilegios (root)
            try {
                s = new ServerSocket( 9999 );
            } catch( IOException e ) {
            	logeo (e + " No se pudo abrir el socket");
                }    	
        	
            // Creamos el objeto desde el cual atenderemos y aceptaremos
            // las conexiones de los clientes y abrimos los canales de  
            // comunicación de entrada y salida
            try {
                cliente = s.accept();
                sIn = new DataInputStream( cliente.getInputStream() );
                sOut = new PrintStream( cliente.getOutputStream() );
                logeo ("Se incia la comunicacion TCP/IP");
                
                // Cuando recibamos datos, se los devolvemos al cliente
                // que los haya enviado
                while( estadoServicio )
                    {
                    texto = sIn.readLine();
            //        sOut.println( texto );
                    if (texto != null)
                    {
                    	logeo(texto);
                    	
                    	pos1 = texto.indexOf("#",0)-1;
                    	pos2 = texto.indexOf("#",pos1+2)-1;
                    	
                    	if (pos1 > 1){
                    	tiempo = Integer.parseInt(texto.substring(0, pos1)) ;
                    	humedad = Float.parseFloat(texto.substring(pos1+2, pos2));
                    	temperatura = Float.parseFloat(texto.substring(pos2+2, texto.length()));
                    	addDatasetValue(tiempo, humedad, temperatura);
                    	
                    	}
                    	//addDatasetValue(1, 2, 10);
                    	
                    }
                    }
            } catch( IOException e ) {
            	logeo (e + " No se pudo atender peticiones");
                }
        	
        	logeo("Thread : " + getName() + " INTERRUMPIDO...");
        	
        	try {
                sIn.close();
                sOut.close();
                cliente.close();
                s.close();
            } catch( IOException e ) {
            	logeo (e + " No se pudo cerrar el socket");
                }
        	
        }
        
}

	
    /**
* Creates a sample dataset.
*
* @return a sample dataset.
*/
private  XYDataset createDataset() {

this.series1.add(0, 0);
this.series2.add(0, 0);

XYSeriesCollection dataset = new XYSeriesCollection();
dataset.addSeries(series1);
dataset.addSeries(series2);

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




/**
* Agreega valores al dataset.
*
*/
private void addDatasetValue (int tiempo,float humedad, float temperatura)

{
	
	series1.add(tiempo, humedad);
	series2.add(tiempo, temperatura);
	
	
}
}
