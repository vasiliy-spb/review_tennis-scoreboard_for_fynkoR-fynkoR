package pet.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateMatchRequest {

    // Этот класс можно сделать record-ом. Это значительно сократит шаблонный код.

    @NotBlank(message = "First player name should be not empty")
    @Size(min = 3, max = 20, message = "First player name should be between 3 and 20 characters")
    private String firstPlayerName;

    @NotBlank(message = "Second player name should be not empty")
    @Size(min = 3, max = 20, message = "Second player name should be between 3 and 20 characters")
    private String secondPlayerName;

    public CreateMatchRequest(){}

    public CreateMatchRequest(String firstPlayerName, String secondPlayerName) {
        this.firstPlayerName = firstPlayerName;
        this.secondPlayerName = secondPlayerName;
    }

    public String getFirstPlayerName() {
        return firstPlayerName;
    }

    public void setFirstPlayerName(String firstPlayerName) {
        this.firstPlayerName = firstPlayerName;
    }

    public String getSecondPlayerName() {
        return secondPlayerName;
    }

    public void setSecondPlayerName(String secondPlayerName) {
        this.secondPlayerName = secondPlayerName;
    }
}
