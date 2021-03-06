
// entrada y salida
import java.util.Scanner;

import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
// excepciones
import java.io.IOException;
// hebras y sockets
import java.net.Socket;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Procesos implements Runnable{
    // variables que entrega el server
    final Scanner entradaDatos;
    final PrintStream salidaDatos;
    final Socket socket;
    private File log;
    private String contenido;
    private BufferedWriter bw;
    private FileWriter fw;
    private String ip;
    private DateFormat hourdateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private Date date = new Date();
    
    public Procesos(Socket socket, Scanner entradaDatos, PrintStream salidaDatos){
        this.entradaDatos = entradaDatos;
        this.salidaDatos = salidaDatos;
        this.socket = socket;
    }

    public void run() {
        String mensaje;
        // variable para envio de archivos
        FileOutputStream fos = null;
        File archivo;
        // handshake
        // envio un mensaje
        salidaDatos.println("Servidor: Hola Cliente");
        log = new File("log.txt");

        // leo un mensaje
        mensaje = entradaDatos.nextLine();
        System.out.println(mensaje);

        //ciclo mientras recibo mensajes
        while (true) {
            try {

                mensaje = entradaDatos.nextLine();
                ip = socket.getRemoteSocketAddress().toString(); 
                System.out.println(ip + " " + mensaje);
            
                // Exit, termina el cliente
                if(mensaje.equals("Exit")){
                    this.socket.close();
                    break;
                }
                // LS
                else if (mensaje.equals("ls")) {
                    date = new Date();
                    contenido = hourdateFormat.format(date) +"       command              "+ip+" ls";
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
                    
                    File folder = new File(".");
                    File[] ListOfFiles = folder.listFiles();
                    // System.out.println(String.valueOf(ListOfFiles.length));
                    // entrego la cantidad de mensajes que enviare para imprimirlos
                    salidaDatos.println(String.valueOf(ListOfFiles.length));
                    
                    for (int i = 0; i < ListOfFiles.length; i++){
                        if(ListOfFiles[i].isFile()){
                            salidaDatos.println("Archivo "+ ListOfFiles[i].getName());
                        }
                        else if(ListOfFiles[i].isDirectory()){
                            salidaDatos.println("Carpeta " + ListOfFiles[i].getName());
                        }
                    }
                    
                    date = new Date();
                    contenido = hourdateFormat.format(date) +"       response             "+"servidor envia respuesta a "+ip;
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
                } 
                // GET
                else if(mensaje.matches("^get [a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*$")){ // comando get
                    mensaje = mensaje.substring(4); // obtengo el nombre del archivo
                    // envio el mensaje
                    
                    // escribo en archivo
                    date = new Date();
                    contenido = hourdateFormat.format(date) +"       command              "+ip+" get "+mensaje;
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

                    try {
                        // variables a usar
                        archivo = new File(mensaje);
                        byte[] bytearray = new byte[(int)archivo.length()];
                        // entrada y salida
                        // fis = new FileInputStream(archivo);
                        DataInputStream bis = new DataInputStream(new FileInputStream(archivo));
                        bis.readFully(bytearray, 0, bytearray.length);

                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                        // envio los datos
                        dos.writeLong(bytearray.length);                      
                        dos.write(bytearray, 0, bytearray.length);
                        dos.flush();
                        // cierro lo que no necesito
                        bis.close();
                        // termino de enviar el archivo
                        date = new Date();
                        contenido = hourdateFormat.format(date) +"       response             "+"servidor envia respuesta a "+ip;
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

                    } catch (Exception e) {
                        System.err.println("Error en el envio del archivo");
                        salidaDatos.println("Error al enviar el archivo " + mensaje);
                    }
                }
                // DELETE
                else if(mensaje.matches("^delete [a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*$")){ // comando delete
                    mensaje = mensaje.substring(7);
                    //System.out.println("archivo es "+mensaje);
                    // se escribe en el log lo que se recibio
                    date = new Date();
                    contenido = hourdateFormat.format(date) +"       command              "+ip+" delete "+mensaje;
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
                    // elimina el archivo
                    File file = new File("./"+mensaje);
                    if (file.delete()){ 
                        salidaDatos.println("Se elimino " + mensaje);
                    }
                    else {
                        salidaDatos.println("Error al eliminar el archivo " + mensaje);
                    }
                    // escribe que se envia respuesta
                    date = new Date();
                    contenido = hourdateFormat.format(date) +"       response             "+"servidor envia respuesta a "+ip;
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
                    
                }
                // PUT
                else if(mensaje.matches("^put [a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*$")){ // comando put
                    mensaje = mensaje.substring(4);
                    int bytesread;

                    // escribe en log que es un put
                    date = new Date();
                    contenido = hourdateFormat.format(date) +"       command              "+ip+" put "+mensaje;
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

                    // VARIABLE PARA RECIBIR BYTES
                    DataInputStream entradad = new DataInputStream(socket.getInputStream());
                    fos = new FileOutputStream(mensaje);
                    
                    // recibo el tamaño del archivo a trabajar:
                    long tamanio = Long.parseLong(entradaDatos.nextLine());
                    System.out.print("Tamaño archivo: ");
                    System.out.println(tamanio);
                    // guardo en un arreglo lo que va llegando
                    byte[] buffer = new byte[1024];
                    int iteraciones =  (int) tamanio/1024 + 1;
                    System.out.println("entrando for");
                    for (int i = 0; i <= iteraciones; i++){
                        System.out.println("iteraciones");  
                        if (i == iteraciones){
                            int falta = (iteraciones-1)*1024; 

                            bytesread = entradad.read(buffer, 0, falta);

                            fos.write(buffer,0,bytesread);
                        }
                        else{
                            bytesread = entradad.read(buffer, 0, 1024);
                            fos.write(buffer,0,bytesread);
                        }
                        System.out.println(i);
                    }
                    fos.close();
                    System.out.println("archivo listo");

                    // escribe en log la respuesta
                    date = new Date();
                    contenido = hourdateFormat.format(date) +"       response             "+"servidor envia respuesta a "+ip;
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
                }else{ 
                    salidaDatos.println("Mensaje no valido: " + mensaje);
                }
            } catch (Exception e) {
                System.err.println("No se pudo obtener el mensaje");
                break;
            }
        }
        try {
            this.entradaDatos.close();
            this.salidaDatos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
