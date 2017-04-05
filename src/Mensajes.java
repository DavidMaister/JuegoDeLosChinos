/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author David
 */
public class Mensajes {
    
    String mensaje;
    
    //cabecera de los mensajes
    public static String mLogin = "log";
    public static String mLoginOk = "lok";
    public static String mLoginError = "lerr";
    public static String mMaquina = "maq";
    public static String mRondas = "ron";
    public static String mRondasOk = "rok";
    public static String mChinos = "chi";
    public static String mApuesta = "apu";
    public static String mGanador = "gan";
    public static String mFin= "fin";
    
    public Mensajes(){
        mensaje = "";
    }
    //mensaje de login, este contiene el alias del usuario
    public String mensajeLogin(String alias){
        mensaje = mLogin+" "+alias+"\n";
        
        return mensaje;
    }
    
    public String mensajeLoginOk(){
        mensaje = mLoginOk+" "+"LoginOk"+"\n";
        return mensaje;
    }
    
    public String mensajeMaquina(){
        mensaje = mMaquina+" "+"\n";
        return mensaje;
    }
    
    public String mensajeRondas(int n){
        mensaje = mRondas+" "+n+" "+"\n";
        return mensaje;
    }
    
    public String mensajeRondasOk(){
        mensaje = mRondasOk+" "+"\n";
        return mensaje;
    }
    
    public String mensajeChinos(int n){
        mensaje = mRondas+" "+n+" "+"\n";
        return mensaje;
    }
    
    public String mensajeApuesta(int n){
        mensaje = mApuesta+" "+n+" "+"\n";
        return mensaje;
    }
    
    public String mensajeGanador(int n){
        mensaje = mGanador+" "+n+" "+"\n";
        return mensaje;
    }
}
