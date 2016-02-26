/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package comunicacion;

import java.io.FileReader;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.speech.Central;
import javax.speech.EngineModeDesc;
import javax.speech.recognition.Recognizer;
import javax.speech.recognition.Result;
import javax.speech.recognition.ResultAdapter;
import javax.speech.recognition.ResultEvent;
import javax.speech.recognition.ResultToken;
import javax.speech.recognition.RuleGrammar;

/**
 *
 * @author Cristian
 */

public class Microfono extends ResultAdapter implements Runnable{
    
    private boolean stop = false;
    private String palabra;
    private Recognizer reconocedorVoz;
    private BlockingQueue queue;
    
    public Microfono(BlockingQueue q){
        queue=q;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
    
    @Override
    public void resultAccepted(ResultEvent re){
        try {
            Result res = (Result) (re.getSource());
            ResultToken tokens[] = res.getBestTokens();
            
            for (int i = 0; i < tokens.length; i++) {
                palabra = tokens[i].getSpokenText();
                System.out.println(palabra);
                if(palabra.equals("ocho")){
                    stop=true;
                    queue.offer("!");
                    System.out.println("!!!"+stop);
                    
                }
                else if(palabra.equals("uno")){
                    //ADELANTE
                    queue.offer("-");
                }
                else if(palabra.equals("dos")){
                    //ATRAS
                    queue.offer("+");
                }
                else if(palabra.equals("cinco")){
                    //IZQUIERDA
                    queue.offer("*");
                }
                else if(palabra.equals("seis")){
                    //DERECHA
                    queue.offer("/");
                }
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        } catch (Throwable ex) {
            Logger.getLogger(Microfono.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reconocerVoz() {
        try {
            reconocedorVoz = Central.createRecognizer(new EngineModeDesc(Locale.ROOT));
            reconocedorVoz.allocate();

            FileReader gramatica = new FileReader("e:/U/VIIISemestre/Lab3/MEGAS/Gramatica.txt");

            RuleGrammar reglaGramatica = reconocedorVoz.loadJSGF(gramatica);
            reglaGramatica.setEnabled(true);

             reconocedorVoz.addResultListener(new Microfono(queue));
             reconocedorVoz.commitChanges();
             reconocedorVoz.requestFocus();
             queue.offer("Reconocimiento de voz activado");
        } catch (Exception e) {
            System.out.println("Error en " + e.toString());
            queue.offer("No se pudo iniciar el reconocimiento");
        }
    }

    @Override
    public void run() {
       if(!stop){
            this.reconocerVoz();
        }
    }
}
