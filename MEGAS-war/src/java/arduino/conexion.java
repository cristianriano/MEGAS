
package arduino;

import comunicacion.Microfono;
import comunicacion.serial;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Cristian
 */
@ManagedBean
@SessionScoped
public class conexion {

    private serial comm;
    private Microfono microfono;
    private Thread voz;
    private BlockingQueue queueVoz = new LinkedBlockingQueue();
    private BlockingQueue queue = new LinkedBlockingQueue();
    private String mensaje;
    private boolean conectado=false;
    private String velocidad;

    public String getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(String velocidad) {
        this.velocidad = velocidad;
    }
    
    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }
    
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public conexion() {
        mensaje=" ";
    }
    
    public void iniciar(){
        comm = new serial(queue);
        comm.start();
        comm.setMsj("6");
        conectado=true;
    }
    
    public void iniciarVoz(){
        microfono = new Microfono(queueVoz);
        voz = new Thread(microfono);
        voz.start();
        
    }
    
    public void detenerVoz(){
        microfono.setStop(true);
        FacesContext msj = FacesContext.getCurrentInstance();  
        msj.addMessage(null, new FacesMessage("Detenido"));
    }
    
    public void adelante(){
        comm.setMsj("-");
    }
    
    public void izquierda(){
        comm.setMsj("*");
    }
    
    public void derecha(){
        comm.setMsj("/");
    }
    
    public void atras(){
        comm.setMsj("+");
    }
    
    public void enviarVelocidad(){
        int v = new Integer(Integer.parseInt(velocidad));
        if(v>=0 && v<=9){
            comm.setMsj(velocidad);
        }
        else{
            queue.offer("Ingrese una velocidad valida");
        }
    }
    
    public void desactivar(){
        comm.setMsj("d");
    }
    
    public void kill(){
        conectado=false;
        comm.kill();
        queue.offer("Arduino desconectado");
    }
    
    public void mensaje(){
        String m = (String) queue.poll();
        if(m!=null){
            System.out.println("-->"+m);
            mensaje=m;
            FacesContext msj = FacesContext.getCurrentInstance();  
            msj.addMessage(null, new FacesMessage("Megas dice: "+m));
        }
    }
    
    public void enviarVoz(){
        String m = (String) queueVoz.poll();
        if(m!=null){
            System.out.println(m);
            if(m.equals("!")){
                queue.offer(m);
            }
            else if(!m.equals("+") && !m.equals("-") && !m.equals("/") && !m.equals("*") && !m.equals("d")){
                voz.stop();
                queue.offer("Reconocimiento detenido");
                return;
            }
            else{
                comm.setMsj(m);
            }
        }
        mensaje();
    }
}
