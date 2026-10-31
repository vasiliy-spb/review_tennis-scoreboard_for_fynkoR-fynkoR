package pet.project.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pet.project.enums.PlayerPosition;
import pet.project.exceptions.SetIsAlreadyCompletedException;

import static org.junit.jupiter.api.Assertions.*;

public class SetTest {

    // Дублирующуюся логику, приводящую тестируемый объект в нужное состояние стоит вынести во вспомогательные методы.

    // В тестах тоже можно выносить повторяющиеся и важные значения в константы, чтобы их смысл был понятнее.

    private Set set;

    @BeforeEach
    public void init(){
        set = new Set();
    }

    @Test
    public void booleanIsFinishedIfSetJustCreated(){
        assertFalse(set.isSetCompleted());
    }

    @Test
    public void onePointDoesNotChangeGamesCount(){
        set.addPointTo(PlayerPosition.FIRST);
        assertEquals(0, set.getCounterFirstPlayer());
        assertEquals(0, set.getCounterSecondPlayer());
    }

    @Test
    public void firstPlayerWinGameCounterIncreased(){
        for(int i = 0; i < 4; i++){
            set.addPointTo(PlayerPosition.FIRST);
        }
        assertEquals(1, set.getCounterFirstPlayer());
    }

    @Test
    public void secondPlayerWinGameCounterIncreased(){
        for(int i = 0; i < 4; i++){
            set.addPointTo(PlayerPosition.SECOND);
        }
        assertEquals(1, set.getCounterSecondPlayer());
    }

    @Test
    public void winByScore6To0(){
        for(int i = 0; i < 4*6; i++){
            set.addPointTo(PlayerPosition.FIRST);
        }
        assertTrue(set.isSetCompleted());
    }

    @Test
    public void winByDifferenceOf2Point(){
        for(int i = 0; i < 4*5; i++){
            set.addPointTo(PlayerPosition.FIRST);
        }
        for(int i = 0; i < 4*4; i++){
            set.addPointTo(PlayerPosition.SECOND);
        }

        for(int i = 0; i < 4; i++){
            set.addPointTo(PlayerPosition.FIRST);
        }
        assertTrue(set.isSetCompleted());
    }
    @Test
    public void noWinByDifferenceOf1Point(){
        for(int i = 0; i < 4*5; i++){
            set.addPointTo(PlayerPosition.FIRST);
        }
        for(int i = 0; i < 4*5; i++){
            set.addPointTo(PlayerPosition.SECOND);
        }

        for(int i = 0; i < 4; i++){
            set.addPointTo(PlayerPosition.FIRST);
        }
        assertFalse(set.isSetCompleted());
    }
    @Test
    public void gameContinueWhenSetScore5To5(){
        for(int i = 0; i < 4*5; i++){
            set.addPointTo(PlayerPosition.FIRST);
        }
        for(int i = 0; i < 4*5; i++){
            set.addPointTo(PlayerPosition.SECOND);
        }
        assertFalse(set.isSetCompleted());
    }
    @Test
    public void tieBreakWhenScore6To6(){
        for(int i = 0; i < 4*5; i++){
            set.addPointTo(PlayerPosition.FIRST);
        }
        for(int i = 0; i < 4*5; i++){
            set.addPointTo(PlayerPosition.SECOND);
        }
        for(int i = 0; i < 4; i++){
            set.addPointTo(PlayerPosition.FIRST);
        }
        for(int i = 0; i < 4; i++){
            set.addPointTo(PlayerPosition.SECOND);
        }

        assertTrue(set.isTieBreak());
    }
    @Test
    public void scoreDoesNotIncreaseWhenTieBreak(){
        for(int i = 0; i < 4*5; i++){
            set.addPointTo(PlayerPosition.FIRST);
        }
        for(int i = 0; i < 4*5; i++){
            set.addPointTo(PlayerPosition.SECOND);
        }
        for(int i = 0; i < 4; i++){
            set.addPointTo(PlayerPosition.FIRST);
        }
        for(int i = 0; i < 4; i++){
            set.addPointTo(PlayerPosition.SECOND);
        }
        set.addPointTo(PlayerPosition.FIRST);
        assertEquals(6, set.getCounterFirstPlayer());
        assertEquals(6, set.getCounterSecondPlayer());

    }
    @Test
    public void setCompletedAfterTieBreak(){
        for(int i = 0; i < 4*5; i++){
            set.addPointTo(PlayerPosition.FIRST);
        }
        for(int i = 0; i < 4*5; i++){
            set.addPointTo(PlayerPosition.SECOND);
        }
        for(int i = 0; i < 4; i++){
            set.addPointTo(PlayerPosition.FIRST);
        }
        for(int i = 0; i < 4; i++){
            set.addPointTo(PlayerPosition.SECOND);
        }

        for(int i = 0; i < 7; i++){
            set.addPointTo(PlayerPosition.FIRST);
        }
        assertTrue(set.isSetCompleted());
    }
    @Test
    public void givePointInFinishedSet(){
        for(int i = 0; i < 4*6; i++){
            set.addPointTo(PlayerPosition.FIRST);
        }
        assertThrows(SetIsAlreadyCompletedException.class, () -> set.addPointTo(PlayerPosition.FIRST));
    }

}
