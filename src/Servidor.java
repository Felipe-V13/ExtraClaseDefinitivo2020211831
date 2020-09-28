
/**
 * Se importan las bibliotecas y variables que se necesitan en el programa*/
import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * Se crea la clase Servidor
 * La varaible MarcoServidor es creada, como default se establece el close de la ventan*/
public class Servidor  {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        MarcoServidor mimarco=new MarcoServidor();

        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
/**
 * Se crea la clase MarcoServidor que hace un extends de Jframe, al igual que hace un implemnets de Runnable
 * Se crea la ventana deL servidor, se establece el tamaño de esta
 * Se establece el area de texto de la ventana
 * Se establece un  hilo para que el programa corra en la ventana*/
class MarcoServidor extends JFrame implements Runnable {

    public MarcoServidor(){

        setBounds(1200,300,280,350);

        JPanel milamina= new JPanel();

        milamina.setLayout(new BorderLayout());

        areatexto=new JTextArea();

        milamina.add(areatexto,BorderLayout.CENTER);

        add(milamina);

        setVisible(true);
        Thread mihilo= new Thread (this);
        mihilo.start();

    }



    /**Se establece el @Override permite que la clase hija, implente acciones de la clase padre, en este caso sería la socket conexion al server 4999
     * Las variables String son definidas, al igual que el PaqueteEnvio*/

    @Override
    public void run (){
        //ServerSocket servidor = new Server Socket(4999);
        try{
            ServerSocket servidor = new ServerSocket(4999);

            String  nick, ip, mensaje;

            PaqueteEnvio paquete_recibido;


            /**Se ejecuta un while(true) para que el código sea reptido un número inidefinido de veces, hasta que haya un break o algo parecido
             * se definen las variables bases del programa , como paquete_datos, paquete_recibido, nick, ip, mensaje
             * */
            while(true) {

                Socket misocket = servidor.accept();

                ObjectInputStream paquete_datos = new ObjectInputStream(misocket.getInputStream());

                paquete_recibido=(PaqueteEnvio) paquete_datos.readObject();

                nick = paquete_recibido.getNick();

                ip=paquete_recibido.getIp();

                mensaje = paquete_recibido.getMensaje();

                areatexto.append("\n" + nick + ":" + mensaje + " para " + ip);

                Socket enviaDestinatario = new Socket(ip,9090);

                ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());

                paqueteReenvio.writeObject(paquete_recibido);

                paqueteReenvio.close();

                enviaDestinatario.close();

                misocket.close();
            }



/**@Exception IOException e */
        } catch (IOException e) {
            e.printStackTrace();
/**@Exception IOException e */
        } catch (ClassNotFoundException e){
            e.printStackTrace();

        }

    }
    private	JTextArea areatexto;
}
