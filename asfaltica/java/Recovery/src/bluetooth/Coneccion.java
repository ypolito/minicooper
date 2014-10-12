package bluetooth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;


public class Coneccion implements DiscoveryListener  {

	
	//object used for waiting
	private static Object lock=new Object();

	//vector containing the devices discovered
	public static Vector vecDevices=new Vector();
	public static Vector<String> vecDevicesNames=new Vector<String>();

	public static String connectionURL=null;
	
	private DiscoveryAgent agent;

	private RemoteDevice remoteDevice;
	
	
	public StreamConnection streamConnection;

    public Coneccion() throws BluetoothStateException {   
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			agent = localDevice.getDiscoveryAgent();
	
    }
    
    public void buscarDispositivos () throws BluetoothStateException 
    {
    	System.out.println("Device Inquiry Starting. ");
		agent.startInquiry(DiscoveryAgent.GIAC, this);
    	
		try {
			synchronized(lock){
			lock.wait();
			}
			}
			catch (InterruptedException e) {
			e.printStackTrace();
			}
		//System.out.println("Device Inquiry Completed. ");
		//frame.textArea.append("Device Inquiry Completed. "+"\r\n");

		//print all devices in vecDevices
		int deviceCount=vecDevices.size();

		if(deviceCount > 0){
		//print bluetooth device addresses and names in the format [ No. address (name) ]
		//System.out.println("Bluetooth Devices: ");
		//frame.textArea.append("Bluetooth Devices: "+"\r\n");
		for (int i = 0; i < deviceCount; i++) {
		remoteDevice=(RemoteDevice)vecDevices.elementAt(i);
		//System.out.println((i+1)+". "+remoteDevice.getBluetoothAddress()+" ("+remoteDevice.getFriendlyName(true)+")");
		//frame.textArea.append((i+1)+". "+remoteDevice.getBluetoothAddress()+" ("+remoteDevice.getFriendlyName(true)+")"+"\r\n");
		}
		}
    }
    
    
    
    public void buscarServicio(int index) throws BluetoothStateException
    {
    	RemoteDevice remoteDevice=(RemoteDevice)vecDevices.elementAt(index-1);
    	UUID[] uuidSet = new UUID[1];
    	uuidSet[0]=new UUID("1101",true);
    	    	
    	agent.searchServices(null,uuidSet,remoteDevice,this);

    	try {
    	synchronized(lock){
    	lock.wait();
    	}
    	}
    	catch (InterruptedException e) {
    	e.printStackTrace();
    	}
    }
    
    public void abrirConeccion () throws IOException
    {
    	streamConnection=(StreamConnection)Connector.open(connectionURL);
    }
    
    
    public void enviarDato (String dato) throws IOException
    {
    	
    	OutputStream outStream=streamConnection.openOutputStream();
    	PrintWriter pWriter=new PrintWriter(new OutputStreamWriter(outStream));
    	pWriter.write(dato);
    	pWriter.flush();
    }
    
    public String recibirDato() throws IOException
                   
    {
    	System.out.println("...read response");
    	InputStream inStream=streamConnection.openInputStream();
    	BufferedReader bReader2=new BufferedReader(new InputStreamReader(inStream));
    	String lineRead=bReader2.readLine();
    	System.out.println("Data Recived Completed. ");
		return lineRead;
    	
    	
    }
    
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
    	//add the device to the vector
    	if(!vecDevices.contains(btDevice)){
    	vecDevices.addElement(btDevice);
    	try {
    		vecDevicesNames.addElement( btDevice.getBluetoothAddress()+" ("+btDevice.getFriendlyName(true)+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	}
    	}



    	public void inquiryCompleted(int discType) {
    			synchronized(lock){
    				System.out.println("Device Inquiry Completed. ");
    					lock.notify();
    			}
    	}

    	//implement this method since services are not being discovered
    	public void serviceSearchCompleted(int transID, int respCode) {
    	synchronized(lock){
    	lock.notify();
    	}
    	}


	//implement this method since services are not being discovered
	public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
	if(servRecord!=null && servRecord.length>0){
	connectionURL=servRecord[0].getConnectionURL(0,false);
	}
	synchronized(lock){
	lock.notify();
	}
	}

}
