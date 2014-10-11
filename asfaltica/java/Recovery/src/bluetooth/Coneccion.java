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

	private static String connectionURL=null;
	
	private DiscoveryAgent agent;

	private RemoteDevice remoteDevice;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

    public Coneccion() throws BluetoothStateException {   
    	//display local device address and name
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			agent = localDevice.getDiscoveryAgent();
			
    }
    
    public void iniciarBusqueda () throws BluetoothStateException 
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

	@Override
	public void serviceSearchCompleted(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
		// TODO Auto-generated method stub
		
	}

}
