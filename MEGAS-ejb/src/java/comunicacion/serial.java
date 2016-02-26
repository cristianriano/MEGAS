package comunicacion;

import Arduino.Arduino;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.BlockingQueue;
/**
 *
 * @author Cristian
 */
public class serial extends Thread{
    
    private Arduino arduino = new Arduino();
    private BlockingQueue queue;
    private boolean stop=false;
    private String msj;
    private SerialPortEventListener evento = new SerialPortEventListener() {
    
    @Override
    public void serialEvent(SerialPortEvent spe) {
            //System.out.println("Event");
            if(arduino.MessageAvailable()){
                System.out.println("mensaje");
                msj=arduino.PrintMessage();
                queue.offer(msj);
            }
        }
    };
    
    public serial(BlockingQueue q){
        queue=q;
        int ports;
        try {
            ports=arduino.SerialPortsAvailable();
            System.out.println(ports);
            /*for(int x=0; x<=ports; x++){
                System.out.println(x);
                System.out.println(arduino.NameSerialPortAt(x));
            }*/
            arduino.ArduinoRXTX("COM6", 2000, 9600, evento);
            queue.offer("Conexion exitosa");
        } catch (Exception ex) {
            ex.printStackTrace();
            queue.offer("No se pudo conectar con MEGAS");
        }
    }
    
    public void enviar(String mensaje){
        
        try {
            //System.out.println("Enviando");
            arduino.SendData(mensaje);
        } catch (Exception ex) {
            queue.offer("No se pudo enviar");
            Logger.getLogger(serial.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run(){
        while(!stop){
        }
    }
    public void setMsj(String s){
        enviar(s);
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
    public void kill(){
        arduino.KillArduinoConnection();
    }
}