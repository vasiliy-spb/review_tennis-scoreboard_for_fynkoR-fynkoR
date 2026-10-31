package pet.project.dto;

public class AddPointRequest {

    // Этот класс можно сделать record-ом

    // Нет аннотаций валидации над этим полем
    private String name;

    public AddPointRequest(){}

    public AddPointRequest(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
