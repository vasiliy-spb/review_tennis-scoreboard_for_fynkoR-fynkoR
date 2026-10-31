package pet.project.dto;

import java.util.UUID;

public class CreateMatchResponse {

    // Этот класс можно сделать record-ом. Это значительно сократит шаблонный код.

    private UUID id;

    public CreateMatchResponse(UUID id){
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
