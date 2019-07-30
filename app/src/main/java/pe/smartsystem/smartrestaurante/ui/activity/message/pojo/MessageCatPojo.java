package pe.smartsystem.smartrestaurante.ui.activity.message.pojo;

public class MessageCatPojo {

    private int id;
    private String name;

    public MessageCatPojo(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
