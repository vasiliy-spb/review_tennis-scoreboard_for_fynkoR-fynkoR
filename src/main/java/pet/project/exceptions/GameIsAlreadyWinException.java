package pet.project.exceptions;

public class GameIsAlreadyWinException extends RuntimeException{

    // "Гейм уже выигран" — это суждение со стороны победителя,
        // а исключению, которое выбрасывается при попытке добавить очко в завершённом гейме,
        // больше подошло бы название GameAlreadyFinishedException.

    public GameIsAlreadyWinException() {
        super("Game is already win !");
    }
}
