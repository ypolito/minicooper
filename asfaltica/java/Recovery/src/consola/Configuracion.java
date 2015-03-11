package consola;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.FileDialog;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JTextField;
import javax.swing.JInternalFrame;
import javax.swing.JFileChooser;




public class Configuracion extends JDialog {
	private JLabel lblSelPuerto;
	private JList list;
    private String[] portNames = puertoCOM.Coneccion.buscarDispositivos();
    public static Stiffness stifness;
    private JTextField textField;
    private String choosertitle = "Directorio exportacion";
    private JTextField textField_1;
    
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Configuracion dialog = new Configuracion();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Configuracion() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			getContentPane().add(tabbedPane, BorderLayout.CENTER);
			{
				JPanel panel = new JPanel();
				tabbedPane.addTab("Puerto COM", null, panel, null);
				{
					lblSelPuerto = DefaultComponentFactory.getInstance().createLabel("Seleccione el puerto");
					lblSelPuerto.setBounds(30, 11, 161, 14);
				}
				panel.setLayout(null);
				panel.add(lblSelPuerto);
				{
					JScrollPane scrollPane = new JScrollPane();
					scrollPane.setBounds(30, 30, 231, 155);
					panel.add(scrollPane);
					{
						list = new JList(portNames);
						list.addListSelectionListener(new ListSelectionListener() {
							public void valueChanged(ListSelectionEvent arg0) {
								// Si se selecciona con el mouse, se descartan los eventos previos
								if (list.getValueIsAdjusting() != true) {String stringValue =  (String)list.getSelectedValue();
								stifness.agregarLog("Puerto COM "+stringValue);}
							}
						});
						list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						scrollPane.setViewportView(list);
					}
				}
			}
			{
				JPanel panel = new JPanel();
				tabbedPane.addTab("Exportar", null, panel, null);
				panel.setLayout(null);
				{
					JLabel lblComentarios = DefaultComponentFactory.getInstance().createLabel("Comentarios");
					lblComentarios.setBounds(126, 151, 60, 14);
					panel.add(lblComentarios);
				}
				{
					JLabel lblNewJgoodiesLabel = DefaultComponentFactory.getInstance().createLabel("Archivo");
					lblNewJgoodiesLabel.setBounds(10, 11, 36, 14);
					panel.add(lblNewJgoodiesLabel);
				}
				
				textField = new JTextField();
				textField.setBounds(56, 8, 206, 20);
				panel.add(textField);
				textField.setColumns(10);
				
				JButton btnNewButton = new JButton("New button");
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setCurrentDirectory(new java.io.File("."));
						fileChooser.setDialogTitle(choosertitle);
						fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						fileChooser.setAcceptAllFileFilterUsed(false);
						
						//int seleccion = fileChooser.showOpenDialog(textField);
						
						if (fileChooser.showSaveDialog(textField) == JFileChooser.APPROVE_OPTION) {
							   System.out.println("getCurrentDirectory(): " 
							      +  fileChooser.getCurrentDirectory());
							   System.out.println("getSelectedFile() : " 
							      +  fileChooser.getSelectedFile());
							   }
							 else {
							   System.out.println("No Selection ");
							   }
						
						/*
						 * 
						 *  chooser = new JFileChooser(); 
 chooser.setCurrentDirectory(new java.io.File("."));
 chooser.setDialogTitle(choosertitle);
 chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
 //
 // disable the "All files" option.
 //
 chooser.setAcceptAllFileFilterUsed(false);

						 * 
						*/
						
						
						//int seleccion = fileChooser.showSaveDialog(textField);
						/*FileDialog dialogoArchivo; 
						JFrame mf = new JFrame();
						dialogoArchivo = new FileDialog(mf,"Lista de Archivos desde Frame", FileDialog.SAVE); 
						dialogoArchivo.show();*/
						
						String nombre="";
					    File guarda =fileChooser.getSelectedFile();
					    //if(guarda !=null)
					    	if(1 ==2)
					    
					    {  
					    	try {
					    	/*guardamos el archivo y le damos el formato directamente,
					    	    * si queremos que se guarde en formato doc lo definimos como .doc*/
					    	    FileWriter  save=new FileWriter(guarda+".txt");
					    	    save.write(textField.getText());
					    	    save.close();
					    	    JOptionPane.showMessageDialog(null,
					    	         "El archivo se a guardado Exitosamente",
					    	             "Información",JOptionPane.INFORMATION_MESSAGE);
					    	}
					    	catch(IOException ex)
					    	  {
					    	   JOptionPane.showMessageDialog(null,
					    	        "Su archivo no se ha guardado",
					    	           "Advertencia",JOptionPane.WARNING_MESSAGE);
					    	  }
					    	    
					    }
					
					}
				});
				btnNewButton.setBounds(264, 106, 89, 23);
				panel.add(btnNewButton);
				
				textField_1 = new JTextField();
				textField_1.setBounds(75, 39, 187, 20);
				panel.add(textField_1);
				textField_1.setColumns(10);
				
				JLabel lblDirectorios = DefaultComponentFactory.getInstance().createLabel("Directorio");
				lblDirectorios.setBounds(10, 36, 92, 14);
				panel.add(lblDirectorios);
				
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	
	public void setConsola(Stiffness consola)
    {
    	stifness  = consola;
    	
    }
}
