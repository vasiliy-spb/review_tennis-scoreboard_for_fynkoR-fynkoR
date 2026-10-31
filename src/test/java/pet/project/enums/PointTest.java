package pet.project.enums;

import org.junit.jupiter.api.Test;
import pet.project.exceptions.NextPointAfterAdvantageException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PointTest {

    // Нет тестов, на нормальное поведение метода next.

    private Point point;

    @Test
    public void shouldBeAfterAdvantageComeException(){
        point = Point.ADVANTAGE;
        assertThrows(NextPointAfterAdvantageException.class, () -> {
            point.next();
        });
    }
}
