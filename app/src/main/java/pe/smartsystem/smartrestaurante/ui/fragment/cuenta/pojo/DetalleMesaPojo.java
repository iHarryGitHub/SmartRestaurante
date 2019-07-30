package pe.smartsystem.smartrestaurante.ui.fragment.cuenta.pojo;

import java.io.Serializable;

public class DetalleMesaPojo implements Serializable {
    private String cantidad;
   // private String pu;
    private double total;
  //  private String nombre;
    private String nomproducto;
    private String idpro;

    private String precxprod;

    public DetalleMesaPojo() {
    }

    public String getPrecxprod() {
        return precxprod;
    }

    public void setPrecxprod(String precxprod) {
        this.precxprod = precxprod;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }




    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getNomproducto() {
        return nomproducto;
    }

    public void setNomproducto(String nomproducto) {
        this.nomproducto = nomproducto;
    }

    //    public String getNombre() {
//        return nombre;
//    }
//
//    public void setNombre(String nombre) {
//        this.nombre = nombre;
//    }

    public String getIdpro() {
        return idpro;
    }

    public void setIdpro(String idpro) {
        this.idpro = idpro;
    }
}
