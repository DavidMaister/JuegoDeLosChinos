
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author David
 */
public class ProtocoloCliente {
    
    /**
     * Constructor del cliente.
     * @param direccionServidor Dirección o nombre del servidor.
     * @param puerto  Puerto donde escucha el servidor.
     */
    public ProtocoloCliente(String direccionServidor, int puerto) {
        Socket socketConexion;
        
        try {
            // Abrimos la conexión.
            socketConexion=new Socket(direccionServidor, puerto);
            
                // Obtenemos los canales de entrada y salida:
                PrintWriter out= new PrintWriter(socketConexion.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socketConexion.getInputStream()));
        
                // Para leer de la línea de comandos:
                BufferedReader inConsola = new BufferedReader(new InputStreamReader(System.in));
        
                // Inicializamos variables para poder mandar mensajes
                Mensajes mensajes = new Mensajes();
                String mensaje;
                
                
                //leemos el nombre de usuario
                System.out.println("Introduce tu alias: ");
                String alias=inConsola.readLine();
                alias=alias.toLowerCase();
            
                
                //Creamos el mensaje y lo enviamos
                mensaje = mensajes.mensajeLogin(alias);
                out.println(mensaje);
                out.flush();
                
                // Recibe la respuesta:
                String respuesta=in.readLine();
                System.out.println("El servidor dice: "+respuesta);
                
                // Interpretamos los campos del mensaje:
                String[] campos = respuesta.split(" ");
                
            // Si es un error, lo comentamos:
            if(campos[0].compareTo("error")==0){
                System.out.println("Error! Has enviado una selección incorrecta.");
            }       
                
            in.close();
            out.close();
            
        } catch (IOException ex) {
            Logger.getLogger(ProtocoloCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
