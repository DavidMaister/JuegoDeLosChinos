/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author David
 */
public class Servidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int puerto=1234;
        
        // Por si quisiéramos pasarle argumentos por la línea de comandos:
        System.out.println("Servidor para juego de los chinos a su servicio");
        if(args.length>0){
            puerto=Integer.parseInt(args[0]);
        }
        
        // Creamos una objeto de esta clase, pasando como argumento el puerto donde debe escuchar:
        System.out.println("Empieza el protocolo");
        new ProtocoloServidor(puerto);
        System.out.println("Termina el protocolo");
    }
    
}
