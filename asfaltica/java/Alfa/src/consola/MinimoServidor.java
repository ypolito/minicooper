package consola;
import java.net.*;
import java.io.*;

public class MinimoServidor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerSocket s = null;
        Socket cliente = null;

        DataInputStream sIn;
        PrintStream sOut;

        String texto;

        // Abrimos una conexión con breogan en el puerto 9999
        // No podemos elegir un puerto por debajo del 1023 si no somos
        // usuarios con los máximos privilegios (root)
        try {
            s = new ServerSocket( 9999 );
        } catch( IOException e ) {
            }

        // Creamos el objeto desde el cual atenderemos y aceptaremos
        // las conexiones de los clientes y abrimos los canales de  
        // comunicación de entrada y salida
        try {
            cliente = s.accept();
            sIn = new DataInputStream( cliente.getInputStream() );
            sOut = new PrintStream( cliente.getOutputStream() );

            // Cuando recibamos datos, se los devolvemos al cliente
            // que los haya enviado
            while( true )
                {
                texto = sIn.readLine();
                sOut.println( texto );
                }
        } catch( IOException e ) {
            System.out.println( e );
            }
        }

}
