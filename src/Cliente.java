//@authorLuisFelipeVargasJiménez

//se importan las bibliotecas y variables que necesitamso
import javax.swing.*;
import java.awt.event.*;
import java.awt.TextArea;
import java.net.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.ObjectInputStream;

/**
 * Se crea una clase pública a la cual se le denomina cliente
 * @version Cliente
 * @MarcoCliente, se crea dicha variable*/

public class Cliente {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        MarcoCliente mimarco=new MarcoCliente();

        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}

/**
 * Se crea la Clase MarcoCLiente, esta es una extensión de JFrame
 * las dimensiones de la ventana son creadas en este apartado
 * se toma a setVisible como true*/
class MarcoCliente extends JFrame{

    public MarcoCliente(){

        setBounds(600,300,280,350);

        LaminaMarcoCliente milamina=new LaminaMarcoCliente();

        add(milamina);

        setVisible(true);
    }

}
/**
 * Se crea la clase LaminaMarcoCliente que hace un extends Jpanel, esta a su vez en un hilo por ende se tiene que implementar Runnable
 * Se le añade un espacio para el nick name del cliente
 * Se le agrega un Jlabel para el nombre de la ventana que será CHAT
 * Además se le agrega un espacio para agregar a la ip con la cual se quiere enlazar
 * SE crea un boton "Enviar"*/
class LaminaMarcoCliente extends JPanel implements Runnable {


    public LaminaMarcoCliente() {
        nick = new JTextField(5);

        add(nick);


        JLabel texto = new JLabel("-CHAT-");

        add(texto);
        ip = new JTextField(8);
        add(ip);

        campochat = new JTextArea(12, 30);

        add(campochat);

        campo1 = new JTextField(20);

        add(campo1);

        miboton = new JButton("Enviar");

        EnviaTexto mievento = new EnviaTexto();

        miboton.addActionListener(mievento);

        add(miboton);

        Thread mihilo = new Thread(this);

        mihilo.start();

    }
    /**
     * @version EnviaTexto, esta implementa ActionListener, para estar siempre  la escucha del servidor
     * @param ActionEvent se crea el evento del socket, en el cual tiene la ip del servidor y la ip del cliente
     * @Exception UnknowHostException cunado el host está vacío */

    private class EnviaTexto implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO-generated method stub
            //System.out.println(campo1.getText());
            try {
                Socket misocket = new Socket("127.0.0.1", 4999);

                PaqueteEnvio datos = new PaqueteEnvio();

                datos.setNick(nick.getText());

                datos.setIp(ip.getText());

                datos.setMensaje(campo1.getText());

                ObjectOutputStream paquete_datos = new ObjectOutputStream(misocket.getOutputStream());

                paquete_datos.writeObject(datos);

                misocket.close();

                /*DataOutputStream flujo_salida=new DataOutputStream(misocket.getOutputStream());
                flujo_salida.writeUTF(campo1.getText());
                flujo_salida.close();*/

            } catch (UnknownHostException e1) {
                //TODO Auto-generated catch Bloc
                e1.printStackTrace();

            } catch (IOException e1) {
                //TODO Auto-generated catch Block

                System.out.println(e1.getMessage());
            }

        }

    }

/**se establecen los campos para el campo1, el nick name, y el ip de la persona con quien quiera comunicarse
 * Se crea el espacio para el campochat
 * además del espacio */
    private JTextField campo1, nick, ip;

    private JTextArea campochat;

    private JButton miboton;
/**
 * Se establece el @Override para que se pueda ejecutar el try del Server Socket
 * Se establece un flujo de entrada para que los mensajes se vayan actualizando conforme los envía tanto el cliente como el servidor
 * Se establecen las variables para que al cliente para que en la pantalla de texto salga el nick, y su mensaje*/
    @Override
    public void run() {
        try {

            ServerSocket servidor_cliente = new ServerSocket(9090);

            Socket cliente;

            PaqueteEnvio paqueteRecibido;
            while (true) {
                cliente = servidor_cliente.accept();

                ObjectInputStream flujoentrada = new ObjectInputStream(cliente.getInputStream());

                paqueteRecibido = (PaqueteEnvio) flujoentrada.readObject();

                campochat.append("\n" + paqueteRecibido.getNick() + ":" + paqueteRecibido.getMensaje());


            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
}
/**
 * Se crea la clase PaqueteEnvio con la implemetacion de Serializable. Con la finalidad de que converta los objetos en bytes y depúes pueda recuperarlos
 * Se crean los Trings nick, ip y mensaje
 * Cada uno de estos se va actulizando conforme el timpo transcurrido
 * Lo único que sí se va a ctualizar ocn el timepo son los mensajes, porque la ip al igual que el nick el clinete lo va a tener que poner de primero*/

class PaqueteEnvio implements Serializable{
    private String nick, ip, mensaje;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
