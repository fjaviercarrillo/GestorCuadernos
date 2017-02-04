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
public class Aplicador {
    int orden;
    String nombre, NIF, ROPO;
    boolean esBasico;
    
    public Aplicador(int orden, String nombre, String NIF, String ROPO, boolean esBasico) throws NumberFormatException {
        this.orden = orden;
        this.nombre = nombre;
        this.NIF = NIF;
        this.ROPO = ROPO;
        this.esBasico = esBasico;
        if (!checkNIF())
            throw new NumberFormatException();
    }
    
    public int getOrden() {
        return orden;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getROPO() {
        return ROPO;
    }
    
    public boolean isBasico() {
        return esBasico;
    }
    
    public boolean checkNIF() {
        if (NIF.length() != 9) {
            return false;
        } else {
            try {
                Integer.parseInt(NIF.substring(0, 7));
            } catch (NumberFormatException e) {
                return false;
            }
            return NIF.charAt(8) >= 'A' && (NIF.charAt(8) <= 'Z' || NIF.charAt(8) >= 'a') && NIF.charAt(8) <= 'z';
        }
    }
}
