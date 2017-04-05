
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
    
    
    //variables que indican los diferentes estados
    private final int introducirAlias = 1;
    private final int autenticado = 2;
    private final int vsMaquina = 3;
    private final int negRondas = 4;
    private final int mandarChinos = 5;
    private final int mandarApuesta = 6;
    private final int resultado = 7;
    private int nRondas;
    /**
     * Constructor del cliente.
     * @param direccionServidor Dirección o nombre del servidor.
     * @param puerto  Puerto donde escucha el servidor.
     */
    public ProtocoloCliente(String direccionServidor, int puerto) {
        Socket socketConexion;
        int estado = introducirAlias;
        boolean salir = false;
        
        
        try {
            // Abrimos la conexión.
            socketConexion=new Socket(direccionServidor, puerto);
            
                // Obtenemos los canales de entrada y salida:
                PrintWriter out= new PrintWriter(socketConexion.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socketConexion.getInputStream()));
        
                // Para leer de la línea de comandos:
                BufferedReader inConsola = new BufferedReader(new InputStreamReader(System.in));
        
                // Inicializamos variables para poder mandar mensajes
                Mensajes mensajeaEnviar = new Mensajes();
                String mensaje;
                String[] campos;
                
                
                
                while(!salir){
                    switch(estado){
                        //este estado corresponde al login
                        case introducirAlias:
                            //leemos el nombre de usuario
                            System.out.println("Introduce tu alias: ");
                            String alias=inConsola.readLine();
                            alias=alias.toLowerCase();
                            
                            //Creamos el mensaje y lo enviamos
                            mensaje = mensajeaEnviar.mensajeLogin(alias);
                            enviarMensaje(mensaje, out);

                            campos = leerPeticion(in);

                            //comprobamos si el login es correcto y pasamos al siguiente estado
                            if(campos[0].compareTo(Mensajes.mLoginOk) == 0){
                                System.out.println("   Login correcto ");
                                estado = autenticado;
                            }
                        break;

                        //se decide si se juega contra maquina o no, pero solo implementaremos que juega contra la maquian
                        //asi que no se toma ninguna decision aqui
                        case autenticado:
                            System.out.println("Jugar contra maquina o jugador");
                            mensaje = mensajeaEnviar.mensajeMaquina();
                            enviarMensaje(mensaje, out);
                            estado = vsMaquina;
                        break;
                        
                        //estado donde se decide el numero de rondas
                        case vsMaquina:
                            //leemos numero de rondas
                            System.out.println("Numero de rondas: ");
                            nRondas = Integer.parseInt(inConsola.readLine());
                            if(nRondas >10 || nRondas <= 0){ //no se aceptan mas de 10 rondas ni negativos, por defecto tres rondas
                                nRondas = 3;
                            }
                            
                            //creamos el mensaje con nº rondas y lo enviamos
                            mensaje = mensajeaEnviar.mensajeRondas(nRondas);
                            enviarMensaje(mensaje, out);
                            System.out.println("Mandamos mensaje nRondas");
                            estado = negRondas;
                            
                        break;
                        
                        //se recibe la confirmacion de las rondas
                        case negRondas:
                            //leemos la confirmacion
                            campos = leerPeticion(in);
                            System.out.println("Mensaje que recibimos de confirmacion: "+campos[0]);
                            
                            if(campos[0].compareTo(Mensajes.mRondasOk) == 0){
                                System.out.println("Rondas confirmadas, que empiece el juego");
                                estado = mandarChinos;
                            }
                            
                            
                        break;
                    }
                    
                }
                
            /*// Si es un error, lo comentamos:
            if(campos[0].compareTo("error")==0){
                System.out.println("Error! Has enviado una selección incorrecta.");
            } */      
                
            in.close();
            out.close();
            
        } catch (IOException ex) {
            Logger.getLogger(ProtocoloCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    //clase para recibir los mensajes enviados por el servidor
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
    
    // clase para enviar mensajes al servidor
    private void enviarMensaje(String mensaje, PrintWriter out){
        // Enviamos la respuesta:
        out.println(mensaje);
        out.flush();
    }
}
