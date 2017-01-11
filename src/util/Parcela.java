/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author ALCAOLIVA
 */
public class Parcela {
    private SimpleIntegerProperty idParcela;
    private SimpleDoubleProperty sizeParcela;
    
    public Parcela(int idParcela, double sizeParcela) {
        this.idParcela = new SimpleIntegerProperty(idParcela);
        this.sizeParcela = new SimpleDoubleProperty(sizeParcela);
    }
    
    public SimpleIntegerProperty getIdParcela() {
        return idParcela;
    }
    
    public SimpleDoubleProperty getSizeParcela() {
        return sizeParcela;
    }
    
    public void addToSizeParcela(double newSize) {
        this.sizeParcela.add(newSize);
    }
}
