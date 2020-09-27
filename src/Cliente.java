
import javax.swing.*;
import java.awt.event.*;
import java.awt.TextArea;
import java.net.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;



public class Cliente {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        MarcoCliente mimarco=new MarcoCliente();

        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}


class MarcoCliente extends JFrame{

    public MarcoCliente(){

        setBounds(600,300,280,350);

        LaminaMarcoCliente milamina=new LaminaMarcoCliente();

        add(milamina);

        setVisible(true);
    }

}

class LaminaMarcoCliente extends JPanel{

    public LaminaMarcoCliente(){
        nick = new JTextField (5);

        add(nick);


        JLabel texto=new JLabel("-CHAT-");

        add(texto);
        ip= new JTextField(8);
        add(ip);

        campochat = new JTextArea(12,30);

        add(campochat);

        campo1=new JTextField(20);

        add(campo1);

        miboton=new JButton("Enviar");

        EnviaTexto mievento=new EnviaTexto();

        miboton.addActionListener(mievento);

        add(miboton);

    }

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


    private JTextField campo1, nick, ip;

    private JTextArea campochat;

    private JButton miboton;

}


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