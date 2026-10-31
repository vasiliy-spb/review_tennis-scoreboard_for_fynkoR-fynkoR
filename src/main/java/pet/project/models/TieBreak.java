package pet.project.models;

import pet.project.enums.PlayerPosition;
import pet.project.exceptions.GameIsAlreadyWinException;

public class TieBreak implements Scorable{

    // Более интуитивно понятным было бы firstPlayerScore
    private int counterFirstPlayer;

    // Более интуитивно понятным было бы secondPlayerScore
    private int counterSecondPlayer;

    // Название поля типа boolean должно звучать в форме вопроса, ответ на который оно содержит: например, hasWinner.
        // Ещё более подходящим названием было бы isFinished.
    private boolean winner;

    // Константы в Java принято объявлять первыми (самыми верхними) в классе
    private static final int INITIAL_VALUE = 0; // Лучше INITIAL_SCORE
    private static final int WIN_VALUE = 7; // Лучше MIN_SCORE_TO_WIN
    private static final int DIFFERENCE_VALUE = 2; // Лучше MIN_SCORE_DIFFERENCE_TO_WIN


    public TieBreak(){
        this.counterFirstPlayer = INITIAL_VALUE;
        this.counterSecondPlayer = INITIAL_VALUE;
    }

    @Override
    public void addPointTo(PlayerPosition playerPosition) { // Можно назвать аргумент winnerPosition
        if(winner){

            // Для гейма, сета и матча есть отдельные исключения, а для тай-брейка используется тот же класс, что и для гейма.
                // Стоит или создать класс исключения для тай-брейка или сделать один общий класс AlreadyFinishedException
                // и просто из разных классов передавать в него разные сообщения.
            throw new GameIsAlreadyWinException();
        }

        // В классе Game переменные названы winnerPoints/opponentPoints, а здесь counter/opponentCounter.
            // С информативными префиксами winner*/opponent* смысл переменных становится понятнее.
        int counter = (playerPosition == PlayerPosition.FIRST) ? counterFirstPlayer : counterSecondPlayer;
        int opponentCounter = (playerPosition == PlayerPosition.FIRST) ? counterSecondPlayer : counterFirstPlayer;

        counter++;

        // Составные условия из if лучше выносить во вспомогательный метод с понятным названием
        if(counter >= WIN_VALUE && (counter - opponentCounter) >= DIFFERENCE_VALUE){
            winner = true;
        }

        counterFirstPlayer = (playerPosition == PlayerPosition.FIRST) ? counter : opponentCounter;
        counterSecondPlayer = (playerPosition == PlayerPosition.FIRST) ? opponentCounter : counter;
    }

    @Override
    public boolean isFinished() {

        // Если нет конфликта имён, можно обращаться к полям без this.
        return this.winner;
    }

    public int getCounterFirstPlayer() {

        // Здесь обращение к полям без this, а в классе Game через this — это неоднородно.
            // Если нет конфликта имён, можно обращаться к полям без this.
        return counterFirstPlayer;
    }

    public int getCounterSecondPlayer() {

        // Здесь обращение к полям без this, а в классе Game через this — это неоднородно.
            // Если нет конфликта имён, можно обращаться к полям без this.
        return counterSecondPlayer;
    }
}
