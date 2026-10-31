package pet.project.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pet.project.enums.PlayerPosition;
import pet.project.exceptions.GameIsAlreadyWinException;

import static org.junit.jupiter.api.Assertions.*;

public class TieBreakTest {

    // Дублирующуюся логику, приводящую тестируемый объект в нужное состояние стоит вынести во вспомогательные методы.

    // В тестах тоже можно выносить повторяющиеся и важные значения в константы, чтобы их смысл был понятнее.

    private TieBreak tieBreak;

    @BeforeEach
    public void init() {
        tieBreak = new TieBreak();
    }

    @Test
    public void booleanIsFinishedIfTieBreakJustCreated() {
        assertFalse(tieBreak.isFinished());
    }

    @Test
    public void winWhenPlayerScores7Points() {
        for (int i = 0; i < 7; i++) {
            tieBreak.addPointTo(PlayerPosition.FIRST);
        }
        assertTrue(tieBreak.isFinished());
    }

    @Test
    public void winWhenA2PointDifference() {
        for (int i = 0; i < 6; i++) {
            tieBreak.addPointTo(PlayerPosition.FIRST);
        }
        for (int i = 0; i < 5; i++) {
            tieBreak.addPointTo(PlayerPosition.SECOND);
        }
        tieBreak.addPointTo(PlayerPosition.FIRST);
        assertTrue(tieBreak.isFinished());
    }

    @Test
    public void noWinWhenScore7To6() {
        for (int i = 0; i < 6; i++) {
            tieBreak.addPointTo(PlayerPosition.FIRST);
        }
        for (int i = 0; i < 6; i++) {
            tieBreak.addPointTo(PlayerPosition.SECOND);
        }
        tieBreak.addPointTo(PlayerPosition.FIRST);
        assertFalse(tieBreak.isFinished());
    }

    @Test
    public void noWinIfPlayersReached7Points() {
        for (int i = 0; i < 6; i++) {
            tieBreak.addPointTo(PlayerPosition.FIRST);
        }
        for (int i = 0; i < 6; i++) {
            tieBreak.addPointTo(PlayerPosition.SECOND);
        }
        tieBreak.addPointTo(PlayerPosition.FIRST);
        tieBreak.addPointTo(PlayerPosition.SECOND);
        assertFalse(tieBreak.isFinished());
    }

    @Test
    public void tryScoreAPointInFinishedTieBreak() {
        for (int i = 0; i < 6; i++) {
            tieBreak.addPointTo(PlayerPosition.FIRST);
        }
        for (int i = 0; i < 5; i++) {
            tieBreak.addPointTo(PlayerPosition.SECOND);
        }
        tieBreak.addPointTo(PlayerPosition.FIRST);

        assertThrows(GameIsAlreadyWinException.class, () -> tieBreak.addPointTo(PlayerPosition.FIRST));
    }
    @Test
    public void secondPlayerWinAtScore7To0(){
        for (int i = 0; i < 7; i++) {
            tieBreak.addPointTo(PlayerPosition.SECOND);
        }
        assertTrue(tieBreak.isFinished());
    }
    @Test
    public void secondPlayerWinAtScore8To6(){
        for (int i = 0; i < 6; i++) {
            tieBreak.addPointTo(PlayerPosition.FIRST);
        }
        for (int i = 0; i < 6; i++) {
            tieBreak.addPointTo(PlayerPosition.SECOND);
        }
        for (int i = 0; i < 2; i++) {
            tieBreak.addPointTo(PlayerPosition.SECOND);
        }
        assertTrue(tieBreak.isFinished());
    }
}
