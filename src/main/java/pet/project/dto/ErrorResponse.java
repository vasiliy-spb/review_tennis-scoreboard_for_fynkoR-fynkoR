package pet.project.dto;

public class ErrorResponse {

    // Этот класс можно сделать record-ом. Это значительно сократит шаблонный код.

    private String message;
    public ErrorResponse(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
