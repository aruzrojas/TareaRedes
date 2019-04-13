// entrada y salida
import java.util.Scanner;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;
// excepciones
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.IOException;
// sockets y hebras
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Thread;
public class Cliente {
    public static void main(String[] args) throws IOException {
        String mensaje, mensajeterminal;
        Socket socket = new Socket("localhost", 1234);
        Scanner inputterminal;
        

        // entrada y salida de datos
        Scanner entradaDatos = new Scanner(socket.getInputStream()); // entrada
        PrintStream salidaDatos = new PrintStream(socket.getOutputStream()); // salida
        // para los archivos
        InputStream in = null;
        OutputStream out = null;
        // abro el archivo
        File file = null;

        byte[] bytes = new byte[16*1024];

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
            }
            else if(mensaje.matches("^get [a-zA-Z0-9]*\\.[a-zA-Z0-9]*$")){
                mensaje = mensaje.substring(4);

                // recibo el archivo
                in = socket.getInputStream();
                try {
                    out = new FileOutputStream(mensaje);
                } catch (FileNotFoundException e) {
                    try {
                        file = new File(mensaje);
                        file.createNewFile();
                        out = new FileOutputStream(mensaje);
                    } catch (Exception er) {
                        System.err.println("Error al crear el archivo");
                    }
                }

                int count;
                while((count = in.read(bytes)) > 0){
                    out.write(bytes,0,count);
                }
                in.close();
                out.close();

                mensaje = entradaDatos.nextLine();
                System.out.println(mensaje);
            }
            else{
                mensaje = entradaDatos.nextLine();
                System.out.println(mensaje);
            }
        }
        
        System.out.println("Fin de la conexión");
        inputterminal.close();
        entradaDatos.close();
        socket.close();
    }
}
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
