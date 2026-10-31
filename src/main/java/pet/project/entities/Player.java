package pet.project.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name = "Player") // Таблицы обычно называют во множественном числе
public class Player{

    // TODO: Нет индекса по имени для БД (должен быть по ТЗ).
        // Его можно задать или через @Column(unique = true) или через аннотацию над классом
        // Можно задать индекс через аннотацию, чтобы у него было понятное имя — @Table(name = "Player", indexes = @Index(...))

    // Для поля `id` лучше использовать обёртку `Integer`, вместо примитивного типа `int`.
        // `Integer` (обёртка) может быть `null` и для нового объекта поле `id` будет `null` до тех пор, пока Hibernate не присвоит ему значение после сохранения.
        // А `int` (примитив) не может быть `null` и для нового, ещё не сохраненного объекта, поле `id` будет иметь значение по умолчанию `0`.
        // Использование обертки `Integer` является предпочтительным для генерируемых ID, потому что позволяет легко и надёжно определить,
        // является ли сущность новой, просто проверив `if (id == null)`.
        // С использованием примитива `0` может оказаться валидным значением ID (хотя и редко), что создаст путаницу.

    // TODO: сеттеры не нужны — позволяют создать объект с установленным id или изменить имя игрока после создания.

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Аннотации валидации в классе entity могут работать не так,
        // как ожидается (например, срабатывать в момент flush, а не save).
        // К тому же валидация имён игроков уже выполняется на входе этих данных в приложение.
        // Поэтому в этом классе достаточно задавать только ограничения, влияющие на DDL.
    @NotBlank(message = "Name should be not empty !")
    @Size(min = 3, max = 20, message = "Name should be between 3 and 20 characters")
    @Column(name = "name") // Стоит добавить nullable = false и length = 20
    private String name;

    // Спецификация JPA требует наличия конструктора без аргументов для создания экземпляров сущностей,
        // однако ему не обязательно быть `public`. Когда конструктор публичный, он становится частью общедоступного API класса.
        // Это позволяет использовать его для создания "пустых", невалидных объектов (без установки обязательных полей)
        // в любом месте приложения, хотя он предназначен исключительно для внутреннего использования фреймворком (JPA).
        //
        // Хорошим подходом будет ограничить область видимости этого конструктора до `protected`.
        // Это делает его недоступным для прямого вызова из других пакетов, но оставляет видимым для JPA и дочерних классов.
    public Player() {}

    public Player(String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
