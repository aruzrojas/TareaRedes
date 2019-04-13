// entrada y salida
import java.util.Scanner;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.*;

// excepciones
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.IOException;
// sockets y hebras
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Thread;
//para fechas
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Servidor{
    public static void main(String[] args) throws IOException{
        // creo las variables basicas a usar por el server
        Socket socket = null;
        Scanner entradaDatos = null;
        PrintStream salidaDatos = null;
        ServerSocket serversocket = null;
        PoolHebras piscina = null; // piscina de hebras

        //Archivo log
        DateFormat hourdateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String contenido = null;
        String ip = null;
        BufferedWriter bw = null;
        FileWriter fw = null;
        File log = new File("log.txt");
        if(!log.exists()){
            log.createNewFile();
        }
        contenido = "DATE TIME                 EVENT                DESCRIPTION";
        fw = new FileWriter(log.getAbsoluteFile(), true);
        bw = new BufferedWriter(fw);
        bw.write(contenido);
        bw.newLine();

        // creo el socket
        try{
            serversocket = new ServerSocket(1234); 
        } catch(IOException e){
            System.err.println("No se pudo abrir el puerto");
            System.exit(-1);
        }

        try {
            if (bw != null)
                bw.close();
            if (fw != null)
                fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // INICIO DEL THREADPOOL
        piscina = new PoolHebras(10);

        // mkdir filein (para guardar los archivos que ingreso)
        

        while (true) {
            System.out.println("Esperando...");

            try {
                socket = serversocket.accept(); // entrada de un cliente
                ip = socket.getRemoteSocketAddress().toString();
                System.out.println("Cliente en línea" +ip);
                //ip = socket.getRemoteSocketAddres().toString();
                contenido = hourdateFormat.format(date)+ "connection         " +ip+ "         Conexion entrante ";
                fw = new FileWriter(log.getAbsoluteFile(), true);
                bw = new BufferedWriter(fw);
                bw.write(contenido);
                bw.newLine();
                try {
                    if (bw != null)
                        bw.close();
                    if (fw != null)
                        fw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                // creo las variables de entrada y salida de datos
                entradaDatos = new Scanner(socket.getInputStream());
                salidaDatos = new PrintStream(socket.getOutputStream()); 

                // ahora la hebra trabaja con el cliente
                piscina.ejecutar(new Procesos(socket, entradaDatos, salidaDatos));
                //servidor envia respueta a socket.getremotesocketadress().tostring());

            } catch (Exception e) {

                System.err.println("Error en la entrada de un cliente");
                e.printStackTrace();
                
                contenido = hourdateFormat.format(date) +"         error        Conexion rechazada por "+ ip;
                fw = new FileWriter(log.getAbsoluteFile(), true);
                bw = new BufferedWriter(fw);
                bw.write(contenido+ip);
                bw.newLine();
                try {
                    if (bw != null)
                        bw.close();
                    if (fw != null)
                        fw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                socket.close();
            }
        }
        // termino del servidor
        //System.out.println("Fin de la ejecución");
        //serversocket.close();
    }
}

                // ahora la hebra trabaja con el cliente
                control = new ControlCliente(socket, entradaDatos, salidaDatos);

                control.start();

            } catch (Exception e) {
                System.err.println("Error en la entrada de un cliente");
                e.printStackTrace();
                socket.close();
            }
        }
        // termino del servidor
        //System.out.println("Fin de la ejecución");
        //serversocket.close();
    }
}
