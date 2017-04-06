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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProtocoloServidor {
    
    //variables necesarias para el algortimo
    //los distintos estados
    private final int esperandoAlias = 1;
    private final int autenticado = 2;
    private final int negRondas = 3;
    private final int esperandoChinos = 4;
    private final int esperandoApuesta = 5;
    private final int ganador = 6;
    private int nRondas;
    private int chinosCliente;
    private int chinos;
    private int apuesta;
    private int apuestaCliente;
          
     /* Constructor de esta clase servidora, 
     * @param puerto 
     */
    public ProtocoloServidor(int puerto) {
        
        boolean salir = false;
        ServerSocket socketEscucha;
        int estado = esperandoAlias;
        Random rand = new Random();
        
        try {
            
            socketEscucha=new ServerSocket(puerto);
            
            // Mientras que no haya que apagar el servidor:
            
                
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
                
                
                
            while(!salir){    
                switch(estado){
                    //estado que se encarga del login
                    case esperandoAlias: //esperandoAlias
                        System.out.println("...Entra en el case esperando alias...");
                       
                        
                        //leemos la peticion
                        campos = leerPeticion(in);
                        if(campos[0].compareTo(Mensajes.mLogin) == 0){
                        String alias = campos[1];
                        
                        //creamos el mensaje y lo enviamos
                        mensaje = fabricaMensajes.mensajeLoginOk();
                        enviarMensaje(mensaje, out);
                        System.out.println("Usuario registrado con exito como: " +alias);
                        estado = autenticado;
                        }
                    break;
                    
                    //estado donde se dedcide maquina o jugador, pero solo haremos la implementacion para maquina
                    case autenticado:
                        System.out.println("...Entra en el case autenticado...");
                        campos = leerPeticion(in);
                        if(campos[0].compareTo(Mensajes.mMaquina) == 0){
                            System.out.println("El cliente ha elegido jugar contra la maquina");
                            estado = negRondas;
                        }
                    break;
                    
                    //se deciden las rondas que se van a jugar
                    case negRondas: 
                        campos = leerPeticion(in);
                        if(campos[0].compareTo(Mensajes.mRondas) == 0){
                            nRondas = Integer.parseInt(campos[1]); //pasamos el string a entero
                            System.out.println("Numero de rondas: "+nRondas);
                            
                            //aceptamos las rondas y mandamos la confirmacion de rondas
                            mensaje = fabricaMensajes.mensajeRondasOk();
                            enviarMensaje(mensaje, out);
                            System.out.println("Mensaje de confirmacion de ronda: "+mensaje);
                            System.out.println(nRondas+" confirmadas para jugar");
                            estado = esperandoChinos;
                        }
                    break;
                   
                    //recibimos el numero de chinos que elige el usuario
                    case esperandoChinos:
                        campos = leerPeticion(in);
                        System.out.println("...Entra en el estado esperando chinos ");
                        if(campos[0].compareTo(Mensajes.mChinos) == 0){
                            System.out.println("Esperando chinos:  "+campos[0]+" "+campos[1]);
                            chinosCliente = Integer.parseInt(campos[1]);
                            System.out.println("El cliente escoge "+chinosCliente+" chinos.");
                            
                            
                            //el servidor elige su numero de chinos aleatoriamente
                            chinos = rand.nextInt(3);
                            System.out.println("El servidor coge "+chinos+" chinos");
                            estado = esperandoApuesta;
                        }
                    break;
                    
                    //recibimos la apuesta 
                    case esperandoApuesta:
                        campos = leerPeticion(in);
                        if(campos[0].compareTo(Mensajes.mApuesta) == 0){
                            apuestaCliente = Integer.parseInt(campos[1]);
                            System.out.println("El cliente apuesta por "+apuestaCliente+" chinos.");
                            //generamos la apuesta del servidor
                            apuesta = rand.nextInt(3) + chinos;
                            System.out.println("El servidor apuesta por "+apuesta+" chinos");
                            estado = ganador;
                        }
                    break;
                    
                    case ganador:
                    break;
                }
                
                
                
            }
            in.close();
            out.close();
            socketConexion.close();
            
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
