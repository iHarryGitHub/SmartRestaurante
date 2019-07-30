package pe.smartsystem.smartrestaurante.ui.activity.login;

public class Plato {
    private String IdProducto;
    private String IdCategoria;
    private String PrecioUnidad;
    private String Estado;
    private String NombreCategoria;
    private String NombreProducto;

    public Plato() {
    }

    public String getNombreProducto() {
        return NombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        NombreProducto = nombreProducto;
    }

    public String getIdProducto() {
        return IdProducto;
    }

    public void setIdProducto(String idProducto) {
        IdProducto = idProducto;
    }

    public String getIdCategoria() {
        return IdCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        IdCategoria = idCategoria;
    }

    public String getPrecioUnidad() {
        return PrecioUnidad;
    }

    public void setPrecioUnidad(String precioUnidad) {
        PrecioUnidad = precioUnidad;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getNombreCategoria() {
        return NombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        NombreCategoria = nombreCategoria;
    }
}
