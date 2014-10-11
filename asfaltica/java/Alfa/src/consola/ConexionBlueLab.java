package consola;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Vector;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
* A simple SPP client that connects with an SPP server
*/
public class ConexionBlueLab implements DiscoveryListener {

//object used for waiting
private static Object lock=new Object();

//vector containing the devices discovered
private static Vector vecDevices=new Vector();

private static String connectionURL=null;




public static void main(String[] args) throws IOException {
	
	ConexionBlueLab client=new ConexionBlueLab();

   //conexion(client);



}//main


public static void conexion(ConexionBlueLab cliente, ConsolaBlueLab frame) throws IOException {

frame.textArea.append("Busqueda en progreso ...."+"\r\n");

//display local device address and name
LocalDevice localDevice = LocalDevice.getLocalDevice();
//System.out.println("Address: "+localDevice.getBluetoothAddress());
frame.textArea.append("Address: "+localDevice.getBluetoothAddress()+"\r\n");

//miFrame.textArea.setText("Address: "+localDevice.getBluetoothAddress());
//System.out.println("Name: "+localDevice.getFriendlyName());
frame.textArea.append("Name: "+localDevice.getFriendlyName()+"\r\n");

//find devices
DiscoveryAgent agent = localDevice.getDiscoveryAgent();

//System.out.println("Starting device inquiry…");
frame.textArea.append("Starting device inquiry…"+"\r\n");
agent.startInquiry(DiscoveryAgent.GIAC, cliente);


try {
synchronized(lock){
lock.wait();
}
}
catch (InterruptedException e) {
e.printStackTrace();
}

//System.out.println("Device Inquiry Completed. ");
frame.textArea.append("Device Inquiry Completed. "+"\r\n");

//print all devices in vecDevices
int deviceCount=vecDevices.size();

if(deviceCount <= 0){
//System.out.println("No Devices Found .");
frame.textArea.append("No Devices Found ."+"\r\n");
//System.exit(0);
}
else{
//print bluetooth device addresses and names in the format [ No. address (name) ]
//System.out.println("Bluetooth Devices: ");
frame.textArea.append("Bluetooth Devices: "+"\r\n");
for (int i = 0; i < deviceCount; i++) {
RemoteDevice remoteDevice=(RemoteDevice)vecDevices.elementAt(i);
//System.out.println((i+1)+". "+remoteDevice.getBluetoothAddress()+" ("+remoteDevice.getFriendlyName(true)+")");
frame.textArea.append((i+1)+". "+remoteDevice.getBluetoothAddress()+" ("+remoteDevice.getFriendlyName(true)+")"+"\r\n");
}


//System.out.print("Choose Device index: ");
frame.textArea.append("Choose Device index: "+"\r\n");
/*
BufferedReader bReader=new BufferedReader(new InputStreamReader(System.in));
String chosenIndex=bReader.readLine();
int index=Integer.parseInt(chosenIndex.trim());
*/

int index=1;

//check for spp service
RemoteDevice remoteDevice=(RemoteDevice)vecDevices.elementAt(index-1);
UUID[] uuidSet = new UUID[1];
uuidSet[0]=new UUID("1101",true);

//System.out.println("\nSearching for service...");
frame.textArea.append("Searching for service..."+"\r\n");
agent.searchServices(null,uuidSet,remoteDevice,cliente);

try {
synchronized(lock){
lock.wait();
}
}
catch (InterruptedException e) {
e.printStackTrace();
}

if(connectionURL==null){
frame.textArea.append("Device does not support Simple SPP Service."+"\r\n");
//System.out.println("Device does not support Simple SPP Service.");
//System.exit(0);
}

else
{
//
System.out.println("...connect to the server and send a line of text");
StreamConnection streamConnection=(StreamConnection)Connector.open(connectionURL);



//send string
System.out.println("...send string");
OutputStream outStream=streamConnection.openOutputStream();
PrintWriter pWriter=new PrintWriter(new OutputStreamWriter(outStream));
pWriter.write("Test String from SPP Client\r\n");
pWriter.flush();

//read response
System.out.println("...read response");
InputStream inStream=streamConnection.openInputStream();
BufferedReader bReader2=new BufferedReader(new InputStreamReader(inStream));
String lineRead=bReader2.readLine();
System.out.println(lineRead);
}
}
frame.textArea.append("Finaliza el proceso."+"\r\n");
//frame.Dg.start();


}
//main



//methods of DiscoveryListener
public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
//add the device to the vector
if(!vecDevices.contains(btDevice)){
vecDevices.addElement(btDevice);
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

//implement this method since services are not being discovered
public void serviceSearchCompleted(int transID, int respCode) {
synchronized(lock){
lock.notify();
}
}

public void inquiryCompleted(int discType) {
synchronized(lock){
lock.notify();
}



}//end method




}