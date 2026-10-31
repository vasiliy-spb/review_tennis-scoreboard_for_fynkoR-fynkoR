package pet.project.models;

import pet.project.enums.PlayerPosition;
import pet.project.exceptions.SetIsAlreadyCompletedException;

public class Set {

    // В Java существует стандартный интерфейс с именем Set,
        // поэтому для избегания путаницы не стоит так называть собственные классы.

    private int counterFirstPlayer;
    private int counterSecondPlayer;

    // TODO: Хранение поля boolean isSetCompleted вынуждает следить не только за состоянием счёта, но и за этим полем.
        // А Также это нарушает Принцип Единого источника истины (см. файл "ssot-principle.md" в этом же пакете).
        // Источником истины в отношении состояния сета является счёт, а завершён ли он можно вычислять на лету —
        // то есть иметь метод `boolean isFinished()`, а не поле.
    private boolean isSetCompleted;

    // Вместо хранения одного поля лучше иметь коллекцию геймов/тай-брейков и наполнять её по ходу игры.
        // Это больше соответствовало бы реальному теннисному матчу.
        // А также позволило бы не хранить счёт в отдельных переменных, а вычислять его на лету.
    private Scorable currentGame;

    // Хранить флаг для тай-брейка избыточно — создавать объект гейма или тай-брейка можно и без него.
        // А также это создаёт риск рассинхронизации источника истины (счёта) с этим флагом.
    private boolean isTieBreak;

    // Константы в Java принято объявлять первыми (самыми верхними) в классе
    private static final int INITIAL_VALUE_GAME = 0; // Лучше INITIAL_SCORE
    private static final int WIN_VALUE_GAME = 6; // Лучше MIN_SCORE_TO_WIN
    private static final int DIFFERENCE_VALUE_GAME = 2; // Лучше MIN_SCORE_DIFFERENCE_TO_WIN

    public Set(){
        counterFirstPlayer = INITIAL_VALUE_GAME;
        counterSecondPlayer = INITIAL_VALUE_GAME;
        currentGame = new Game();
    }

    // TODO: Логика этого метода излишне усложнена. Тройная вложенность if-else + сложные составные условия в них.
        // Методы, выполняющие большое количество действий стоит разбивать на более простые методы,
        // выполняющие единственную задачу. Такой код будет проще читать и поддерживать.
    public void addPointTo(PlayerPosition playerPosition){ // Можно назвать аргумент winnerPosition
        if(isSetCompleted){
            throw new SetIsAlreadyCompletedException();
        }

        // В классе Game переменные названы winnerPoints/opponentPoints, а здесь counter/opponentCounter.
            // С информативными префиксами winner*/opponent* смысл переменных становится понятнее.
        int counter = (playerPosition == PlayerPosition.FIRST) ? counterFirstPlayer : counterSecondPlayer;
        int opponentCounter = (playerPosition == PlayerPosition.FIRST) ? counterSecondPlayer : counterFirstPlayer;

        if(isTieBreak){
            currentGame.addPointTo(playerPosition);
            if(currentGame.isFinished()){
                counter++;
                isSetCompleted = true;
            }
        }
        else{
            currentGame.addPointTo(playerPosition);
            if(currentGame.isFinished()){
                counter++;

                // Составные условия из if лучше выносить во вспомогательный метод с понятным названием,
                    // а также можно вводить дополнительные поясняющие переменные
                if(counter >= WIN_VALUE_GAME && (counter - opponentCounter >= DIFFERENCE_VALUE_GAME)){
                    isSetCompleted = true;
                }
                else if(counter == WIN_VALUE_GAME && opponentCounter == WIN_VALUE_GAME){
                    currentGame = new TieBreak();
                    isTieBreak = true;
                }
                else{
                    currentGame = new Game();
                }
            }
        }

        counterFirstPlayer = (playerPosition == PlayerPosition.FIRST) ? counter : opponentCounter;
        counterSecondPlayer = (playerPosition == PlayerPosition.FIRST) ? opponentCounter : counter;
    }

    public boolean isSetCompleted() {
        return isSetCompleted;
    }

    public Scorable getCurrentGame() {
        return currentGame;
    }

    public int getCounterFirstPlayer() {
        return counterFirstPlayer;
    }

    public int getCounterSecondPlayer() {
        return counterSecondPlayer;
    }

    // Метод используется только в тестах — это недостаточная причина его существования в коде
    public boolean isTieBreak() {
        return isTieBreak;
    }

}
