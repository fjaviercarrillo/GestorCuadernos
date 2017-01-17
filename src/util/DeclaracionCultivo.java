/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ALCAOLIVA
 */
public class DeclaracionCultivo {
    private double totalSize;
    private int idCliente;
    private int idDeclaracionCultivo;
    private boolean necesitaAsesor;
    private String imgName1;
    private String imgName2;
    
    public DeclaracionCultivo(int idCliente, double totalSize, boolean necesitaAsesor, String imgName) {
        this.totalSize = totalSize;
        this.idCliente = idCliente;
        this.idDeclaracionCultivo = idDeclaracionCultivo;
        this.necesitaAsesor = necesitaAsesor;
        this.imgName1 = imgName;
        this.imgName2 = null;
    }
    
    public DeclaracionCultivo(int idCliente, double totalSize, boolean necesitaAsesor, String imgName1, String imgName2) {
        this.totalSize = totalSize;
        this.idCliente = idCliente;
        this.idDeclaracionCultivo = idDeclaracionCultivo;
        this.necesitaAsesor = necesitaAsesor;
        this.imgName1 = imgName1;
        this.imgName2 = imgName2;
    }
    
    public void addDataToBD() throws SQLException {
        ConexionBD connection = new ConexionBD();
        int intNecesitaAsesor = (necesitaAsesor) ? 1 : 0;
        String query;
        if (imgName2 == null) {
            query = "INSERT INTO DeclaracionesCultivo (idCliente, totalSize, necesitaAsesor, imgName1) VALUES "
                    + "(" + idCliente + ", " + totalSize + ", " + intNecesitaAsesor + ", '" + imgName1 + "')";
        } else {
            query = "INSERT INTO DeclaracionesCultivo (idCliente, totalSize, necesitaAsesor, imgName1, imgName2) VALUES "
                    + "(" + idCliente + ", " + totalSize + ", " + intNecesitaAsesor + ", '" + imgName1 + "', ' " + imgName2 + "')";
        }
        connection.executeQuery(query);
        connection.close();
        setIdDeclaracionCultivo();
    }
    
    private void setIdDeclaracionCultivo() throws SQLException {
        ConexionBD connection = new ConexionBD();
        String query = "SELECT idDeclaracionCultivo FROM DeclaracionesCultivo WHERE idCliente = " + idCliente;
        ResultSet results = connection.selectQuery(query);
        results.next();
        idDeclaracionCultivo = results.getInt("idDeclaracion");
        connection.close();
    }
    
    public double getTotalSize() {
        return totalSize;
    }
    
    public int getIdCliente() {
        return idCliente;
    }
    
    public int getIdDeclaracionCultivo() {
        return idDeclaracionCultivo;
    }
    
    public boolean necesitaAsesor() {
        return necesitaAsesor;
    }
}
