/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        this.necesitaAsesor = necesitaAsesor;
        this.imgName1 = imgName;
        this.imgName2 = "";
    }
    
    public DeclaracionCultivo(int idCliente, double totalSize, boolean necesitaAsesor, String imgName1, String imgName2) {
        this.totalSize = totalSize;
        this.idCliente = idCliente;
        this.necesitaAsesor = necesitaAsesor;
        this.imgName1 = imgName1;
        this.imgName2 = imgName2;
    }
    
    public DeclaracionCultivo(int idDeclaracionCultivo, int idCliente, double totalSize, boolean necesitaAsesor, String imgName1, String imgName2) {
        this.totalSize = totalSize;
        this.idCliente = idCliente;
        this.necesitaAsesor = necesitaAsesor;
        this.imgName1 = imgName1;
        this.imgName2 = imgName2;
        this.idDeclaracionCultivo = idDeclaracionCultivo;
    }
    
    public void addDataToBD(Connection connection) throws SQLException {
        int intNecesitaAsesor = (necesitaAsesor) ? 1 : 0;
        String query = "INSERT INTO DeclaracionesCultivo (idCliente, totalSize, necesitaAsesor, imgName1, imgName2) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idCliente);
            preparedStatement.setDouble(2, totalSize);
            preparedStatement.setInt(3, intNecesitaAsesor);
            preparedStatement.setString(4, imgName1);
            preparedStatement.setString(5, imgName2);
            preparedStatement.executeUpdate();
            setIdDeclaracionCultivo(connection);
        }
    }
    
    private void setIdDeclaracionCultivo(Connection connection) throws SQLException {
        ResultSet results = null;
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT idDeclaracionCultivo FROM DeclaracionesCultivo WHERE idCliente = " + idCliente;
            results = statement.executeQuery(query);
            results.next();
            idDeclaracionCultivo = results.getInt("idDeclaracionCultivo");
        } finally {
            if (results != null) {
                results.close();
            }
        }
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
    
    public String getImg1() {
        return this.imgName1;
    }
    
    public void setTotalSize(double totalSize) {
        this.totalSize = totalSize;
    }
}
