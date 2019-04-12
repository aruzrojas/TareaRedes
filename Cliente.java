// entrada y salida
import java.io.PrintStream;
import java.util.Scanner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import java.io.File;
import java.io.FileInputStream;
public class Cliente {
    public static void main(String[] args) throws IOException {
        String mensaje, mensajeterminal;
        Socket cs = new Socket("127.0.0.1", 1234);
        Scanner inputterminal;

        // entrada y salida de datos
        Scanner entradaDatos = new Scanner(cs.getInputStream()); // entrada
        PrintStream salidaDatos = new PrintStream(cs.getOutputStream()); // salida
        // InputStream in = null;
        // OutputStream out = null;
        // // abro el archivo
        // File file = new File("./../filein/test.txt");

        // byte[] bytes = new byte[16*1024];

        // recibo un mensaje
        mensaje = entradaDatos.nextLine();
        System.out.println(mensaje);  

        // envio un mensaje
        salidaDatos.println("Cliente: Respuesta recibida");      
        inputterminal = new Scanner(System.in);
        // paso de mensajes
        while(true){
            mensajeterminal = inputterminal.nextLine();

            salidaDatos.println(mensajeterminal);
            // veo como tratar la respuesta al comando
            if (mensajeterminal.equals("Exit")) {
                break;
            }else{
                mensaje = entradaDatos.nextLine();
                System.out.println(mensaje);
            }
        }

        // envio el archivo
        // in = new FileInputStream(file);
        // out = cs.getOutputStream();

        // int count;
        // while((count = in.read(bytes)) > 0){
        //     out.write(bytes, 0, count);
        // }
        
        System.out.println("Fin de la conexión");
        inputterminal.close();
        entradaDatos.close();
        cs.close();
    }
}