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
public class Equipo {
    int orden;
    String descripcion, ROMA, fechaAdquisicion, ultimaInspeccion;
    
    public Equipo(int orden, String descripcion, String ROMA, String fechaAdquisicion, String ultimaInspeccion) {
        this.orden = orden;
        this.descripcion = descripcion;
        this.ROMA = ROMA;
        this.fechaAdquisicion = fechaAdquisicion;
        this.ultimaInspeccion = ultimaInspeccion;
    }
    
    public int getOrden() {
        return orden;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public String getROMA() {
        return ROMA;
    }
    
    public String getFechaAdquisicion() {
        return fechaAdquisicion;
    }
    
    public String getUltimaInspeccion() {
        return ultimaInspeccion;
    }
}
