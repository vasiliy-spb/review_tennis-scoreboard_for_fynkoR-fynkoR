package pet.project.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pet.project.enums.PlayerPosition;
import pet.project.enums.Point;
import pet.project.exceptions.GameIsAlreadyWinException;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    // В тестах тоже можно выносить повторяющиеся и важные значения в константы, чтобы их смысл был понятнее.

    // Дублирующуюся логику, приводящую тестируемый объект в нужное состояние стоит вынести во вспомогательные методы.

    private Game game;

    @BeforeEach
    void init(){
        game = new Game();
    }

    @Test
    public void booleanIsFinishedIfGameJustCreated(){
        assertFalse(game.isFinished());
    }

    @Test
    public void shouldBeAfterLoveComeFifteen(){
        game.addPointTo(PlayerPosition.FIRST);
        assertEquals(Point.FIFTEEN, game.getFirstPlayerPoint());
    }

    @Test
    public void shouldBeAfterFifteenComeThirty(){
        game.addPointTo(PlayerPosition.FIRST);
        game.addPointTo(PlayerPosition.FIRST);
        assertEquals(Point.THIRTY, game.getFirstPlayerPoint());
    }
    @Test
    public void shouldBeAfterThirtyComeForty(){
        game.addPointTo(PlayerPosition.FIRST);
        game.addPointTo(PlayerPosition.FIRST);
        game.addPointTo(PlayerPosition.FIRST);
        assertEquals(Point.FORTY, game.getFirstPlayerPoint());
    }

    @Test
    public void shouldBeAfterFortyComeAdvantage(){
        game.addPointTo(PlayerPosition.FIRST);
        game.addPointTo(PlayerPosition.FIRST);
        game.addPointTo(PlayerPosition.FIRST);

        game.addPointTo(PlayerPosition.SECOND);
        game.addPointTo(PlayerPosition.SECOND);
        game.addPointTo(PlayerPosition.SECOND);

        game.addPointTo(PlayerPosition.FIRST);
        assertEquals(Point.ADVANTAGE, game.getFirstPlayerPoint());
    }

    @Test
    public void addThreePointsEachAndOnePointFirstPlayerGameNotIsFinished(){
        for(int i = 0; i < 3; i++){
            game.addPointTo(PlayerPosition.FIRST);
            game.addPointTo(PlayerPosition.SECOND);
        }
        game.addPointTo(PlayerPosition.FIRST);
        assertFalse(game.isFinished());
    }

    @Test
    public void shouldReturnToDeuceWhenOpponentScoresDuringAdvantage(){
        for(int i = 0; i < 3; i++){
            game.addPointTo(PlayerPosition.FIRST);
            game.addPointTo(PlayerPosition.SECOND);
        }
        game.addPointTo(PlayerPosition.FIRST);
        game.addPointTo(PlayerPosition.SECOND);
        assertEquals(Point.FORTY, game.getFirstPlayerPoint());
        assertEquals(Point.FORTY, game.getSecondPlayerPoint());
    }

    @Test
    public void firstPlayerHasAdvantagePlusPointToHimGameIsFinished(){
        for(int i = 0; i < 3; i++){
            game.addPointTo(PlayerPosition.FIRST);
            game.addPointTo(PlayerPosition.SECOND);
        }
        game.addPointTo(PlayerPosition.FIRST);
        game.addPointTo(PlayerPosition.FIRST);
        assertTrue(game.isFinished());
    }

    @Test
    public void score40To30WinWhenDifferenceTwoPoints(){
        for(int i = 0; i < 3; i++){
            game.addPointTo(PlayerPosition.FIRST);
        }
        for(int i = 0; i < 2; i++){
            game.addPointTo(PlayerPosition.SECOND);
        }
        game.addPointTo(PlayerPosition.FIRST);
        assertTrue(game.isFinished());
    }

    @Test
    public void ifPlayerWinPointAt40To0ThenGameIsFinished(){
        for(int i = 0; i < 3; i++){
            game.addPointTo(PlayerPosition.FIRST);
        }
        game.addPointTo(PlayerPosition.FIRST);
        assertTrue(game.isFinished());
    }

    @Test
    public void shouldWinWhenAdvantageIsLostAndRegainedAfterDeuce(){
        for(int i = 0; i < 3; i++){
            game.addPointTo(PlayerPosition.FIRST);
            game.addPointTo(PlayerPosition.SECOND);
        }
        game.addPointTo(PlayerPosition.FIRST);
        game.addPointTo(PlayerPosition.SECOND);
        game.addPointTo(PlayerPosition.FIRST);
        game.addPointTo(PlayerPosition.FIRST);
        assertTrue(game.isFinished());
    }

    @Test
    public void givePointInFinishedGame(){
        for(int i = 0; i < 4; i++){
            game.addPointTo(PlayerPosition.FIRST);
        }
        assertThrows(GameIsAlreadyWinException.class, () -> {
            game.addPointTo(PlayerPosition.FIRST);
        });
    }

    @Test
    public void givePointSecondPlayerAtScore0To40WinToGame(){
        for(int i = 0; i < 3; i++){
            game.addPointTo(PlayerPosition.SECOND);
        }
        game.addPointTo(PlayerPosition.SECOND);
        assertTrue(game.isFinished());
    }

    @Test
    public void score40To40PlusPointSecondPlayerAdvantageInSecondPlayer(){
        for(int i = 0; i < 3; i++){
            game.addPointTo(PlayerPosition.FIRST);
            game.addPointTo(PlayerPosition.SECOND);
        }
        game.addPointTo(PlayerPosition.SECOND);
        assertEquals(Point.ADVANTAGE, game.getSecondPlayerPoint());
    }

    @Test
    public void score40To40PlusTwoPointsSecondPlayerAndWinGame(){
        for(int i = 0; i < 3; i++){
            game.addPointTo(PlayerPosition.FIRST);
            game.addPointTo(PlayerPosition.SECOND);
        }
        game.addPointTo(PlayerPosition.SECOND);
        game.addPointTo(PlayerPosition.SECOND);
        assertTrue(game.isFinished());
    }
}
