/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author ALCAOLIVA
 */
public class Asesor {
    String nombre, NIF, identificacion, tipo;
    
    public Asesor(String nombre, String NIF, String identificacion, String tipo) {
        this.nombre = nombre;
        this.NIF = NIF;
        this.identificacion = identificacion;
        this.tipo = tipo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getNIF() {
        return NIF;
    }
    
    public String getIdentificacion() {
        return identificacion;
    }
    
    public String getTipo() {
        return tipo;
    }
}
