package pet.project.models;

import pet.project.enums.PlayerPosition;
import pet.project.enums.Point;
import pet.project.exceptions.GameIsAlreadyWinException;

public class Game implements Scorable{

    private Point firstPlayerPoint;
    private Point secondPlayerPoint;

    // TODO: Название поля вводит в заблуждение. Это поле служит флагом завершения гейма, а не победы первого игрока.
    private boolean firstPlayerWin;

    public Game(){
        this.firstPlayerPoint = Point.LOVE;
        this.secondPlayerPoint = Point.LOVE;
    }

    @Override
    public void addPointTo(PlayerPosition playerPosition){ // Можно назвать аргумент winnerPosition

        if(firstPlayerWin){
            throw new GameIsAlreadyWinException();
        }

        Point winnerPoints = (playerPosition == PlayerPosition.FIRST) ? firstPlayerPoint : secondPlayerPoint;
        Point opponentPoints = (playerPosition == PlayerPosition.FIRST) ? secondPlayerPoint : firstPlayerPoint;

        // Составные условия из if лучше выносить во вспомогательный метод с понятным названием
        if(winnerPoints == Point.FORTY && (opponentPoints != Point.FORTY && opponentPoints != Point.ADVANTAGE)){
            firstPlayerWin = true;
        }
        else if(winnerPoints == Point.ADVANTAGE){
            firstPlayerWin = true;
        }
        else if (opponentPoints == Point.ADVANTAGE) {
            opponentPoints = Point.FORTY;
        }
        else{
            winnerPoints = winnerPoints.next();
        }

        firstPlayerPoint = (playerPosition == PlayerPosition.FIRST) ? winnerPoints : opponentPoints;
        secondPlayerPoint = (playerPosition == PlayerPosition.FIRST) ? opponentPoints : winnerPoints;
    }

    @Override
    public boolean isFinished() {

        // Если нет конфликта имён, можно обращаться к полям без this.
        return this.firstPlayerWin;
    }

    public Point getFirstPlayerPoint() {

        // Если нет конфликта имён, можно обращаться к полям без this.
        return this.firstPlayerPoint;
    }

    public Point getSecondPlayerPoint() {

        // Если нет конфликта имён, можно обращаться к полям без this.
        return this.secondPlayerPoint;
    }

}
