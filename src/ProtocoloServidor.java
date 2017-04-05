/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author David
 */
/**/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProtocoloServidor {
    
    //variables necesarias para el algortimo
    //los distintos estados
    private final int esperandoAlias = 1;
    private final int autenticado = 2;
    private final int vsMaquina = 3;
    private final int negRondas = 4;
    private final int esperandoChinos = 5;
    private final int esperandoApuesta = 6;
    private final int ganador = 7;
    
          
     /* Constructor de esta clase servidora, 
     * @param puerto 
     */
    public ProtocoloServidor(int puerto) {
        
        boolean salir = false;
        ServerSocket socketEscucha;
        int estado = esperandoAlias;
        
        
        try {
            
            socketEscucha=new ServerSocket(puerto);
            
            // Mientras que no haya que apagar el servidor:
            while(!salir){
                
                // Esperamos una conexión:
                Socket socketConexion=socketEscucha.accept();
                
                // Obtenemos los canales de entrada y salida:
                PrintWriter out= new PrintWriter(socketConexion.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socketConexion.getInputStream()));
                
                System.out.println("...Sirviendo juego de los chinos...");
                //Inicializamos variables para poder enviar y recibir mensajes
                Mensajes fabricaMensajes = new Mensajes();  //clase donde estan los formatos de los mensajes
                String[] campos;      //mensaje que se recibe desde el cliente
                String mensaje;      //mensaje que se manda al cliente
                
                
                
                
                switch(estado){
                    case esperandoAlias: //esperandoAlias
                        System.out.println("Entra en el case esperando alias");
                       
                        
                        //leemos la peticion
                        campos = leerPeticion(in);
                        if(campos[0].compareTo(Mensajes.mLogin) == 0){
                        String alias = campos[1];
                        System.out.println("Usuario registrado como: " +alias);
                        //creamos el mensaje y lo enviamos
                        mensaje = fabricaMensajes.mensajeLoginOk();
                        System.out.println("Mensaje LoginOk que se envia: "+mensaje);
                        enviarMensaje(mensaje, out);
                        estado = autenticado;
                        }
                    break;
                
                }
                
                
                in.close();
                out.close();
                socketConexion.close();
            }
            
            socketEscucha.close();
            
            
        } catch (IOException ex) {
            Logger.getLogger(ProtocoloServidor.class.getName()).log(Level.SEVERE, null, ex);

        }        
    }
    
    private String[] leerPeticion(BufferedReader in){
        // Leemos una petición:
        
        String linea=" ";
        try {
            linea = in.readLine();
        } catch (IOException ex) {
            Logger.getLogger(ProtocoloServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Interpretamos la petición. Para que no haya problema con las letras de la palabra
        // al compararlas con los comandos, las pasamos todas a minúsculas:
        linea = linea.toLowerCase();
        // los comandos vienen  separados por espacios,por lo que los separamos
        String[] campos = linea.split(" ");
          

        return campos;
    }
    
    private void enviarMensaje(String mensaje, PrintWriter out){
        // Enviamos la respuesta:
        out.println(mensaje);
        out.flush();
    }
}
