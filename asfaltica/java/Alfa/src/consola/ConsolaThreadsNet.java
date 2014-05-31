package consola;

import java.awt.BorderLayout;
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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConsolaThreadsNet extends JFrame {

	private JPanel contentPane;
    private JTextArea textArea;
    private SimpleThread miThread;
	static  boolean estadoServicio = false;
    
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
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(toolBar, GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 191, Short.MAX_VALUE)
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
				miThread = new SimpleThread("ComTC/PIP");
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
        	while (estadoServicio) {
        	logeo("Thread : " + getName() + " en ejcucion");
        	if (estadoServicio) {
        	try { sleep(2000); } 
        	catch (InterruptedException e){}
        	logeo("Ciclando... ");
        	}
        	}
        	logeo("Thread : " + getName() + " INTERRUMPIDO...");
        }
        
}
}
