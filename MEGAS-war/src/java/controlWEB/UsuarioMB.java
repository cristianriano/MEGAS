/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlWEB;

import ejb.UsuarioEJB;
import entidades.Usuario;
import ejb.UsuarioEJB;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Cristian
 */
@ManagedBean
@RequestScoped
public class UsuarioMB {

    @EJB
    private UsuarioEJB usuEJB;
    private List<Usuario> usuarios;
    private String login,pass, log, nom, tipo;
    private Usuario usuario;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
    public void crearUsuario(ActionEvent actionEvent){
        FacesContext msj = FacesContext.getCurrentInstance();  
        if(!usuEJB.verificarLogin(log)){
            msj.addMessage(null, new FacesMessage("Login ya en uso")); 
            return;
        }
        Usuario usuario = new Usuario(null, this.log, this.pass, this.nom, this.tipo);
        if(usuEJB.ingresarUsuario(usuario)){
            msj.addMessage(null, new FacesMessage("Creado")); 
            return;
        }
        msj.addMessage(null, new FacesMessage("Imposible crear usuario")); 
    }
    public void eliminar(ActionEvent actionEvent){
        FacesContext msj = FacesContext.getCurrentInstance();  
        Usuario usuario = usuEJB.buscar(this.log);
        if(usuEJB.eliminarUsuario(usuario)){
            msj.addMessage(null, new FacesMessage("Usuario eliminado"));
            return;
            }
        
        msj.addMessage(null, new FacesMessage("No se pudo eliminar"));
    }
    public void buscarUsuario(ActionEvent actionEvent){
        FacesContext msj = FacesContext.getCurrentInstance();  
        
        usuario = usuEJB.buscar(this.log);
        
        if(usuario!=null){
            msj.addMessage(null, new FacesMessage("Usuario encontrado")); 
            this.login=usuario.getLogin();
            this.nom=usuario.getNombre();
            this.tipo=usuario.getTipo();
            return;
        }
        msj.addMessage(null, new FacesMessage("Usuario inexistente")); 
    }
    public String listarUsuarios(){
        usuarios=usuEJB.listarUsuarios();
        System.out.println(usuarios.size());
        return "lista2";
    }
   public void editarUsuario(ActionEvent actionEvent){
        FacesContext msj = FacesContext.getCurrentInstance();  
        usuario = usuEJB.buscar(this.log);
        if(usuario!=null){
            System.out.println(log+login);
            if(!log.equals(login)){
            if(!usuEJB.verificarLogin(login)){
                msj.addMessage(null, new FacesMessage("Login ya en uso")); 
                return;
            }}
            System.out.println(usuario.getPass());
            usuario.setLogin(this.login);
            usuario.setNombre(this.nom);
            usuario.setTipo(this.tipo);
            if(usuEJB.editarUsuario(usuario)){
                msj.addMessage(null, new FacesMessage("Editado")); 
                return;
            }
        }
        msj.addMessage(null, new FacesMessage("Imposible editar")); 
    }
    public UsuarioMB() {
    }
    
}
