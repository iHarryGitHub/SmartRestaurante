package pe.smartsystem.smartrestaurante.ui.activity;

import java.io.Serializable;

public class MesaPojo implements Serializable {

    private int nMesa;
    private String stadomesa;


    public MesaPojo() {
    }

    public MesaPojo(int nMesa, String stadomesa) {
        this.nMesa = nMesa;
        this.stadomesa = stadomesa;
    }

    public int getnMesa() {
        return nMesa;
    }

    public void setnMesa(int nMesa) {
        this.nMesa = nMesa;
    }

    public String getStadomesa() {
        return stadomesa;
    }

    public void setStadomesa(String stadomesa) {
        this.stadomesa = stadomesa;
    }
}