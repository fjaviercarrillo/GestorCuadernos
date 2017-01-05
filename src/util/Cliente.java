/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Sk
 */
public class Cliente {
    private final SimpleStringProperty nombre;
    private final SimpleStringProperty apellidos;
    private final SimpleStringProperty DNI;
    private final SimpleStringProperty direccion;
    private final SimpleIntegerProperty codigoPostal;
    private final SimpleStringProperty localidad;
    private final SimpleStringProperty provincia;
    private final SimpleStringProperty pais;
    private final SimpleBooleanProperty asesor;
    
    public Cliente(String nombre, String apellidos, String DNI, String direccion, int codigoPostal, String localidad, String provincia,
            String pais, boolean asesor){
        this.nombre = new SimpleStringProperty(nombre);
        this.apellidos = new SimpleStringProperty(apellidos);
        this.DNI = new SimpleStringProperty(DNI);
        this.direccion = new SimpleStringProperty(direccion);
        this.codigoPostal = new SimpleIntegerProperty(codigoPostal);
        this.localidad = new SimpleStringProperty(localidad);
        this.provincia = new SimpleStringProperty(provincia);
        this.pais = new SimpleStringProperty(pais);
        this.asesor = new SimpleBooleanProperty(asesor);
    }
    
    public SimpleBooleanProperty necesitaAsesor(){
        return asesor;
    }
    
    public String getNombre(){
        return nombre.get();
    }
    
    public String getApellidos(){
        return apellidos.get();
    }
    
    public String getDNI(){
        return DNI.get();
    }
    
    public String getDireccion(){
        return direccion.get();
    }
    
    public int getCodigoPostal(){
        return codigoPostal.get();
    }
    
    public String getLocalidad(){
        return localidad.get();
    }
 
    public String getProvincia(){
        return provincia.get();
    }
    
    public String getPais(){
        return pais.get();
    }
}
