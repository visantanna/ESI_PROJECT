package com.example.visantanna.leilaoapp.controllers;

/**
 * Created by vinicius on 04/12/17.
 */
import java.math.BigDecimal;

public class RetornoLance {
    private BigDecimal lanceAtual;
    private boolean sucessoLance;

    public BigDecimal getLanceAtual() {
        return lanceAtual;
    }
    public void setLanceAtual(BigDecimal lanceAtual) {
        this.lanceAtual = lanceAtual;
    }
    public boolean isSucessoLance() {
        return sucessoLance;
    }
    public void setSucessoLance(boolean sucessoLance) {
        this.sucessoLance = sucessoLance;
    }
}

