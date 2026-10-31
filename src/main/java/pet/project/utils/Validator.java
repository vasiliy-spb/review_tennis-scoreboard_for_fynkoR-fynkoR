package pet.project.utils;

import pet.project.exceptions.ValidationException;

public class Validator {

    // Класс спроектирован как утилитный, но при этом не объявлен как final и имеет публичный конструктор.

    // Метод не только валидирует, но и нормализует переданное значение. Это нарушает SRP на уровне метода.
    public static Integer validateIntegerPage(Integer page){

        // Параметр page из контроллера никогда не null (есть defaultValue = "1").
        if(page == null){
            return 0;
        }

        // Когда из блока if происходит return, то следующий код можно писать без else.
        else if(page >= 0){
            return page;
        }

        // Когда из блока if происходит return, то следующий код можно писать без else.
        else{
            throw new ValidationException("No valid value page !");
        }
    }
}
