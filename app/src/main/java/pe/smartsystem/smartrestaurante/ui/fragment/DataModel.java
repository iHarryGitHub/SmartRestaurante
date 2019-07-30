package pe.smartsystem.smartrestaurante.ui.fragment;

public class DataModel {

    String plato;
    String categoria;

    public DataModel() {
    }

    public DataModel(String plato) {
        this.plato = plato;
    }

    public String getPlato() {
        return plato;
    }

    public void setPlato(String plato) {
        this.plato = plato;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
