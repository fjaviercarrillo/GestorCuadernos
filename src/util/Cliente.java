/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Sk
 */
public class Cliente {
    private final SimpleStringProperty nombre;
    
    public static boolean CON_ASESOR = true;
    public static boolean SIN_ASESOR = false;
    private String apellidos;
    private String DNI;
    private String direccion;
    private int codigoPostal;
    private String localidad;
    private String provincia;
    private String pais;
    private boolean asesor;
    
    public Cliente(String nombre, String apellidos, String DNI, String direccion, int codigoPostal, String localidad, String provincia,
            boolean asesor){
        this.nombre = new SimpleStringProperty(nombre);
        this.apellidos = apellidos;
        this.DNI = DNI;
        this.direccion = direccion;
        this.codigoPostal = codigoPostal;
        this.localidad = localidad;
        this.provincia = provincia;
        this.pais = "ESPAÑA";
        this.asesor = asesor;
    }
    
    public Cliente(String nombre) {
        this.nombre = new SimpleStringProperty(nombre);
    }
    
    public void setAsesor(boolean asesor){
        this.asesor = asesor;
    }
    
    public boolean necesitaAsesor(){
        return asesor;
    }
    
    public String getNombre(){
        return nombre.get();
    }
    
    public void setApellidos(String apellidos){
        this.apellidos = apellidos;
    }
    
    public String getApellidos(){
        return apellidos;
    }
    
    public void setDNI(String DNI){
        this.DNI = DNI;
    }
    
    public String getDNI(){
        return DNI;
    }
    
    public void setDireccion(String direccion){
        this.direccion = direccion;
    }
    
    public String getDireccion(){
        return direccion;
    }
    
    public void setCodigoPostal(int codigoPostal){
        this.codigoPostal = codigoPostal;
    }
    
    public int getCodigoPostal(){
        return codigoPostal;
    }
    
    public void setLocalidad(String localidad){
        this.localidad = localidad;
    }
    
    public String getLocalidad(){
        return localidad;
    }
    
    public void setProvincia(String provincia){
        this.provincia = provincia;
    }
    
    public String getProvincia(){
        return provincia;
    }
    
    public void setPais(String pais){
        this.pais = pais;
    }
    
    public String getPais(){
        return pais;
    }
}
