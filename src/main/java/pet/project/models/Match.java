package pet.project.models;

import pet.project.enums.PlayerPosition;
import pet.project.exceptions.MatchIsAlreadyCompletedException;
import pet.project.exceptions.MatchIsNotCompletedException;
import pet.project.exceptions.NamePlayerNotInMatchException;

public class Match {

    // Логику сопоставления имени игрока с его стороной можно вынести в отдельный класс (например, ActiveMatch),
        // А в самом Match оставить только работу со счётом (по аналогии с классами для гейма-сета).
        // Так соблюдение SRP станет более строгим.

    // Объект сета отдаётся наружу для получения счёта в нём.
        // Есть несколько подходов — или позволить мапперу
        // получать все объекты моделей и напрямую запрашивать у них счёт,
        // или в верхнем классе (ActiveMatch) создать все методы, возвращающие счёт на каждом этапе.
        // Оба решения компромиссные.
        // Третий вариант — создать классы, хранящие пары значений счёта и возвращать из верхнего уровня (ActiveMatch) их.
        // Этот вариант тоже имеет свои минусы, но зато не отдаёт классы моделей за пределы их слоя.

    private int counterFirstPlayer;
    private int counterSecondPlayer;

    // TODO: Хранение поля boolean isMatchCompleted вынуждает следить не только за состоянием счёта, но и за этим полем.
        // А Также это нарушает Принцип Единого источника истины (см. файл "ssot-principle.md" в этом же пакете).
        // Источником истины в отношении состояния сета является счёт, а завершён ли он можно вычислять на лету —
        // то есть иметь метод `boolean isFinished()`, а не поле.
    private boolean isMatchCompleted;
    private TennisPlayer firstPlayer; // Это поле должно быть final, так как нельзя менять игроков после создания матча.
    private TennisPlayer secondPlayer; // Это поле должно быть final, так как нельзя менять игроков после создания матча.

    // Вместо хранения одного поля лучше иметь коллекцию сетов и наполнять её по ходу игры.
        // Это больше соответствовало бы реальному теннисному матчу.
        // А также позволило бы не хранить счёт в отдельных переменных, а вычислять его на лету.
    private Set set;

    // Константы в Java принято объявлять первыми (самыми верхними) в классе
    private static final int INITIAL_VALUE = 0; // Лучше INITIAL_SCORE
    private static final int WIN_VALUE = 2; // Лучше MIN_SCORE_TO_WIN

    public Match(TennisPlayer firstPlayer, TennisPlayer secondPlayer){
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.counterFirstPlayer = INITIAL_VALUE;
        this.counterSecondPlayer = INITIAL_VALUE;
        set = new Set();
    }

    public void addPointTo(String nameFirstPlayer){ // Можно назвать аргумент winnerName
        if(isMatchCompleted){
            throw new MatchIsAlreadyCompletedException();
        }

        PlayerPosition playerPosition = isFirstPlayer(nameFirstPlayer);

        // В классе Game переменные названы winnerPoints/opponentPoints, а здесь counter/opponentCounter.
            // С информативными префиксами winner*/opponent* смысл переменных становится понятнее.
        int counter = (playerPosition == PlayerPosition.FIRST) ? counterFirstPlayer : counterSecondPlayer;
        int opponentCounter = (playerPosition == PlayerPosition.FIRST) ? counterSecondPlayer : counterFirstPlayer;

        set.addPointTo(playerPosition);
        if(set.isSetCompleted()){
            counter++;
            if(counter >= WIN_VALUE){
                isMatchCompleted = true;
            }
            else{
                set = new Set();
            }
        }

        counterFirstPlayer = (playerPosition == PlayerPosition.FIRST) ? counter : opponentCounter;
        counterSecondPlayer = (playerPosition == PlayerPosition.FIRST) ? opponentCounter : counter;
    }

    // TODO: Название метода не соответствует типу возвращаемого значения
        // и вся сигнатура метода вводит в замешательство (см. файл "pola.md" в этом же пакете).
    public PlayerPosition isFirstPlayer(String nameFirstPlayer){ // Точнее назвать аргумент playerName
        if(firstPlayer.name().equals(nameFirstPlayer)){
            return PlayerPosition.FIRST;
        }
        else if(secondPlayer.name().equals(nameFirstPlayer)){
            return PlayerPosition.SECOND;
        }
        else{
            throw new NamePlayerNotInMatchException(nameFirstPlayer);
        }
    }

    public TennisPlayer getWinner(){
        if(counterFirstPlayer >= 2){ // Для значение 2 есть константа WIN_VALUE
            return firstPlayer;
        }

        // Когда из блока if происходит return, то следующий код можно писать без else.
        else if(counterSecondPlayer >= 2){ // Для значение 2 есть константа WIN_VALUE
            return secondPlayer;
        }

        // Когда из блока if происходит return, то следующий код можно писать без else.
        else{
            throw new MatchIsNotCompletedException();
        }
    }

    public TennisPlayer getSecondPlayer() {
        return secondPlayer;
    }

    public TennisPlayer getFirstPlayer() {
        return firstPlayer;
    }

    public int getCounterFirstPlayer() {
        return counterFirstPlayer;
    }

    public int getCounterSecondPlayer() {
        return counterSecondPlayer;
    }

    public boolean isMatchCompleted() {
        return isMatchCompleted;
    }

    public Set getSet() {
        return set;
    }
}
