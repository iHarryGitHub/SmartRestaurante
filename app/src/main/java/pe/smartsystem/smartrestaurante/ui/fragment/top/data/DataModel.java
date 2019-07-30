package pe.smartsystem.smartrestaurante.ui.fragment.top.data;

public class DataModel {

    String plato;
    String categoria;
    //String pu;
    String PrecXProd;
    String idproducto;
    String idcategoria;
    String destino;
    String nombreCategoria;
    String precioUnidad;
    //String NombreProducto;
    String nombreProducto;

    public DataModel() {
    }

    public DataModel(String plato, String categoria, String pu, String idproducto, String idcategoria, String destino, String nombreCategoria) {
        this.plato = plato;
        this.categoria = categoria;
        this.PrecXProd = pu;
        this.idproducto = idproducto;
        this.idcategoria = idcategoria;
        this.destino = destino;
        this.nombreCategoria = nombreCategoria;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    //    public String getNombreProducto() {
//        return NombreProducto;
//    }
//
//    public void setNombreProducto(String nombreProducto) {
//        NombreProducto = nombreProducto;
//    }

    public String getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(String precioUnidad) {
        this.precioUnidad = precioUnidad;
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


    public String getPrecXProd() {
        return PrecXProd;
    }

    public void setPrecXProd(String precXProd) {
        PrecXProd = precXProd;
    }

    public String getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(String idproducto) {
        this.idproducto = idproducto;
    }

    public String getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(String idcategoria) {
        this.idcategoria = idcategoria;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}
