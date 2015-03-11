package puertoCOM;
import java.util.Scanner;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;
import consola.Stiffness;

public class Coneccion {

	
	public static SerialPort serialPort;
	public static String datoRecibido;
	public static Stiffness stifness;
	public static String comBT;
	 
	/**
	 * @param args
	 */
	
    public Coneccion(String comPort) {
		// TODO Auto-generated constructor stub
    	  serialPort = new SerialPort(comPort);
	}
    
    
    public void setConsola(Stiffness consola)
    {
    	stifness  = consola;
    	
    }
    
    public static String[] buscarDispositivos()
    {
    	String[] portNames = SerialPortList.getPortNames();
    	return portNames;
    }
    
	public void abrirConeccion () 
    {
		try {
            // opening port
            serialPort.openPort();
            
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE);
            
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | 
                                          SerialPort.FLOWCONTROL_RTSCTS_OUT);
            
            
            // writing string to port
                    }
        catch (SerialPortException ex) {
            System.out.println("Error al abrir COM: " + ex);
        }
    }

	public void cerrarConeccion () throws SerialPortException 
    {
		serialPort.closePort();//Close serial port
    }
	
	
	public void enviarDato(String dato) throws SerialPortException{
		 serialPort.writeString(dato);
	}
	
	 public String recibirDato() throws SerialPortException 
	 {
		 serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
		 return "hola";}
	 
	 
	// receiving response from port
    private static class PortReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    // RECIBIR RESPUESTA DESDE EL PUERTO
                    String receivedData = serialPort.readString(event.getEventValue());
                    
                    while (receivedData.indexOf("\n") > -1) {
                    	receivedData = receivedData.replaceFirst("\n", "");
                  }
                    while (receivedData.indexOf("\r") > -1) {
                    	receivedData = receivedData.replaceFirst("\r", "");
                  }
                    /* Comunica la informacion recibida*/
                    stifness.addDatasetPort(receivedData);
                    //System.out.println("Received response from port: " + receivedData);
                }
                catch (SerialPortException ex) {
                    System.out.println("Error in receiving response from port: " + ex);
                }
            }
        }
    }
}
