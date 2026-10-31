package pet.project.exceptions;

import pet.project.enums.Point;

public class NextPointAfterAdvantageException extends RuntimeException{

    // Само название исключения говорит о том, что оно выбрасывается только на Advantage,
        // поэтому можно или не передавать Point в конструктор
        // или переименовать класс в более универсальное название.

    public NextPointAfterAdvantageException(Point point) {
        super("No next score after +" + point); // Лишний знак + внутри строки
    }
}
