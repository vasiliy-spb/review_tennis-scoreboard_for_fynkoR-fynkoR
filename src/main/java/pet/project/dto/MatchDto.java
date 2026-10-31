package pet.project.dto;

public class MatchDto {

    // Этот класс можно сделать record-ом. Это значительно сократит шаблонный код.

    // В объектах DTO лучше не иметь сеттеров без крайней необходимости (которой в этом проекте нет),
        // чтобы не было возможности забыть установить одно из полей.

    private PlayerDto firstPlayer;
    private PlayerDto secondPlayer;
    private String winnerName;

    public MatchDto(){}

    public MatchDto(PlayerDto firstPlayer, PlayerDto secondPlayer, String winnerName) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.winnerName = winnerName;
    }

    public PlayerDto getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(PlayerDto firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public PlayerDto getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(PlayerDto secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }
}
