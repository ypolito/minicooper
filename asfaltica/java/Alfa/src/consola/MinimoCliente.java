package consola;

import java.awt.*;
import java.net.*;
import java.io.*;

public class MinimoCliente {

	/**
	 * Clase para probar comunicaciones TCP/IP con la consola J
	 * @param args
	 * @throws InterruptedException 
	 */
	 public static void main( String args[] ) throws IOException, InterruptedException {
	        int c;
	        Socket s;
	        InputStream sIn;
	        PrintStream sOut;
	        double humedad;
	        double temperatura;

	        // Abrimos una conexión con depserver en el puerto 4321
	        s = new Socket( "localhost", 9999 );

	        // Obtenemos un controlador de fichero de entrada del socket y
	        // leemos esa entrada
	        /* Se comenta este texto del manual
	         * sIn = s.getInputStream();
	        while( ( c = sIn.read() ) != -1 )
	            System.out.print( (char)c );
	         */
	        
	        
	        try {
	            sOut = new PrintStream( s.getOutputStream() );
	            sOut.println("AOK");
	            for (int i=1;i<1000;i++) {
	            	humedad = Math.random() * 100;
	            	temperatura = Math.random() * 20;
	              	
	            sOut.println(i*500+"#"+humedad+"#"+temperatura);
	            Thread.sleep(500); 
	            }
	            
	        } catch( IOException e ) {
	            System.out.println( e );
	            }
	        
	        
	        
	        
	        // Cuando se alcance el fin de fichero, cerramos la conexión y
	        // abandonamos
	        s.close();
	        }

}
