package ejb;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import entidades.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
/**
 *
 * @author Cristian
 */
@Stateless
@LocalBean
public class UsuarioEJB {
    
    @PersistenceContext(unitName = "MEGAS-ejbPU")
    private EntityManager em;
    private final static String clave="megas!RockS.";

    public boolean ingresarUsuario(Usuario u){
        u.setPass(encrypt(u.getPass()));
        try{
            em.persist(u);
        } catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    public List<Usuario> listarUsuarios(){
        Query q = em.createNamedQuery("Usuario.findAll");
        List<Usuario> usuarios = new ArrayList<Usuario>();
        try{
            usuarios= q.getResultList();
        } catch(javax.persistence.NoResultException e){
            return null;
        }
        return usuarios;
        
    }
    
    public boolean eliminarUsuario(Usuario u){
        try{
            em.remove(em.merge(u));
        } catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean editarUsuario (Usuario u){
        
        try{
            em.merge(u);
        } catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public Usuario buscar(String log){
        Query q = em.createNamedQuery("Usuario.findByLogin");
        q.setParameter("login", log);
        Usuario usuario;
        try{
            usuario=(Usuario)q.getSingleResult();
            //usuario.setPass(decrypt(usuario.getPass()));
        } catch(javax.persistence.NoResultException ex){
            ex.printStackTrace();
            return null;
        }
        return usuario;
    }
    
    public boolean verificarLogin(String l){
        Query q = em.createNamedQuery("Usuario.findByLogin");
        q.setParameter("login", l);
        Usuario u;
        try{
            u=(Usuario)q.getSingleResult();
        }
        catch(javax.persistence.NoResultException e){
            return true;
        }
        return false;
    }
    
    public String encrypt(String cadena) {
        StandardPBEStringEncryptor s = new StandardPBEStringEncryptor();
        s.setPassword(clave);
        return s.encrypt(cadena);
    }
    
    public String decrypt(String cadena) {
        StandardPBEStringEncryptor s = new StandardPBEStringEncryptor();
        s.setPassword(clave);
        String devuelve = "";
        try {
            devuelve = s.decrypt(cadena);
        } catch (Exception e) {
        }
        return devuelve;
    } 
    
}
