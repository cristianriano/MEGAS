/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controlWEB;

import ejb.UsuarioEJB;
import entidades.Usuario;
import javax.ejb.EJB;
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
public class accesoMB {
    
    @EJB
    private UsuarioEJB usuEJB;
    private String login,pass,newPass,nombre;
    private Usuario user;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }
    
    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
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
    public String validarUsuario(){
      Usuario usuario;
      usuario = usuEJB.buscar(this.login);
      if(usuario!=null){
          usuario.setPass(usuEJB.decrypt(usuario.getPass()));
          if(usuario.getPass().equals(this.pass)){
              user = usuario;
              if(usuario.getTipo().equals("SI")){
                  return "administrador";
              }
              return "bienvenido";
      }
      }
      return "error";
    }
    
    public void cambiarPass(){
        FacesContext msj = FacesContext.getCurrentInstance();
        System.out.println(pass+"//"+user.getPass());
        if(pass.equals(user.getPass())){
            user.setPass(usuEJB.encrypt(newPass));
            if(usuEJB.editarUsuario(user)){
                msj.addMessage(null, new FacesMessage("Contraseña cambiada")); 
                user.setPass(newPass);
            }
            else{
                msj.addMessage(null, new FacesMessage("No se pudo cambiar contraseña")); 
            }
        }
        else{
            msj.addMessage(null, new FacesMessage("Revise su contraseña actual")); 
        }
    }
    
    public String eliminarCuenta(){
        FacesContext msj = FacesContext.getCurrentInstance();  
        if(user.getPass().equals(pass)){
            if(usuEJB.eliminarUsuario(user)){
                return "index";
            }
            msj.addMessage(null, new FacesMessage("No se pudo eliminar")); 
        }
        else{
            msj.addMessage(null, new FacesMessage("Revise su contraseña")); 
        }
        return "eliminarCuenta";
    }
    
    public void editarPerfil(){
        FacesContext msj = FacesContext.getCurrentInstance();  
        if(usuEJB.verificarLogin(login)){
            user.setLogin(login);
            user.setNombre(nombre);
            if(usuEJB.editarUsuario(user)){
                msj.addMessage(null, new FacesMessage("Perfil editado")); 
            }
        }
        else{
            msj.addMessage(null, new FacesMessage("Login ya esta en uso")); 
        }
    }
    public accesoMB() {
    }
    
}
