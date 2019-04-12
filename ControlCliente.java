// entrada y salida
import java.util.Scanner;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.IOException;

import java.lang.Thread;
import java.net.Socket;
public class ControlCliente extends Thread{
    final Scanner entradaDatos;
    final PrintStream salidaDatos;
    final Socket socket;
    
    public ControlCliente(Socket socket, Scanner entradaDatos, PrintStream salidaDatos){
        this.entradaDatos = entradaDatos;
        this.salidaDatos = salidaDatos;
        this.socket = socket;
    }

    @Override
    public void run() {
        String mensaje;
        // handshake
        // envio un mensaje
        salidaDatos.println("Servidor: Hola Cliente");

        // leo un mensaje
        mensaje = entradaDatos.nextLine();
        System.out.println(mensaje);

        //ciclo mientras recibo mensajes
        while (true) {
            try {
                mensaje = entradaDatos.nextLine();
                System.out.println(mensaje);

                if(mensaje.equals("Exit")){
                    this.socket.close();
                    break;
                }else if (mensaje.equals("Hola")) {
                    salidaDatos.println("Recibi tu hola");
                }else if(mensaje.equals("Mundo")){
                    salidaDatos.println("Recibi tu mundo");
                }else{
                    salidaDatos.println("Mensaje no valido");
                }
            } catch (Exception e) {
                System.err.println("No se pudo obtener el mensaje");
                e.printStackTrace();
                break;
            }
        }
        try {
            this.entradaDatos.close();
            this.entradaDatos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}