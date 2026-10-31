package pet.project.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pet.project.exceptions.MatchIsAlreadyCompletedException;
import pet.project.exceptions.MatchIsNotCompletedException;

import static org.junit.jupiter.api.Assertions.*;

public class MatchTest {

    // Дублирующуюся логику, приводящую тестируемый объект в нужное состояние стоит вынести во вспомогательные методы.

    // В тестах тоже можно выносить повторяющиеся и важные значения в константы, чтобы их смысл был понятнее.

    private Match match;
    private TennisPlayer firstPlayer;
    private TennisPlayer secondPlayer;

    @BeforeEach
    public void init(){
        firstPlayer = new TennisPlayer("firstPlayer");
        secondPlayer = new TennisPlayer("secondPlayer");
        match = new Match(firstPlayer, secondPlayer);
    }

    @Test
    public void matchCreatedWithTwoPlayers(){
        assertEquals(match.getFirstPlayer(), firstPlayer);
        assertEquals(match.getSecondPlayer(), secondPlayer);
    }
    @Test
    public void counterSetsIsZeroIfMatchJustCreated(){
        assertEquals(0, match.getCounterFirstPlayer());
        assertEquals(0, match.getCounterSecondPlayer());
    }
    @Test
    public void booleanIsFinishedIfMatchJustCreated(){
        assertFalse(match.isMatchCompleted());
    }
    @Test
    public void getWinnerUntilWin(){
        assertThrows(MatchIsNotCompletedException.class, () -> match.getWinner());
    }
    @Test
    public void oneAddOnePointDoesNotChangeCounterSet(){
        match.addPointTo(firstPlayer.name());
        assertEquals(0, match.getCounterFirstPlayer());
    }
    @Test
    public void setCompletedCounterIncreased(){
        for(int i = 0; i < 4*6; i++){
            match.addPointTo(firstPlayer.name());
        }
        assertEquals(1, match.getCounterFirstPlayer());
    }
    @Test
    public void score2To0MatchIsCompleted(){
        for(int i = 0; i < 4*6*2; i++){
            match.addPointTo(firstPlayer.name());
        }
        assertTrue(match.isMatchCompleted());
    }
    @Test
    public void score2To1MatchIsCompleted(){
        for(int i = 0; i < 4*6; i++){
            match.addPointTo(firstPlayer.name());
        }
        for(int i = 0; i < 4*6; i++){
            match.addPointTo(secondPlayer.name());
        }
        for(int i = 0; i < 4*6; i++){
            match.addPointTo(firstPlayer.name());
        }
        assertTrue(match.isMatchCompleted());
    }
    @Test
    public void notShouldMatchIsCompletedWhenScore1To1(){
        for(int i = 0; i < 4*6; i++){
            match.addPointTo(firstPlayer.name());
        }
        for(int i = 0; i < 4*6; i++){
            match.addPointTo(secondPlayer.name());
        }
        assertFalse(match.isMatchCompleted());
    }
    @Test
    public void notShouldMatchIsCompletedWhenScore1To0(){
        for(int i = 0; i < 4*6; i++){
            match.addPointTo(firstPlayer.name());
        }
        assertFalse(match.isMatchCompleted());
    }
    @Test
    public void tryAddPointWhenMatchIsCompleted(){
        for(int i = 0; i < 4*6*2; i++){
            match.addPointTo(firstPlayer.name());
        }
        assertThrows(MatchIsAlreadyCompletedException.class, () -> match.addPointTo(firstPlayer.name()));
    }
}
