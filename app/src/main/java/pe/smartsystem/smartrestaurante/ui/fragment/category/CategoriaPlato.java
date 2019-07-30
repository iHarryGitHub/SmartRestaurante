package pe.smartsystem.smartrestaurante.ui.fragment.category;

public class CategoriaPlato {
    private String name;
    private int id;

    public CategoriaPlato() {
    }

    public CategoriaPlato(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
