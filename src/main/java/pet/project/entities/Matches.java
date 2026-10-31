package pet.project.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Matches") // "Matches" является зарезервированным словом в некоторых СУБД.
    // Здесь проблем не будет, но лучше не выбирать такие названия. (см. файл "sql-keywords.md" в этом же пакете)
public class Matches{

    // Стоит добавить проверки, что игроки разные и победитель один из игроков. Например, через аннотацию org.hibernate.annotations.Check над классом.

    // Для поля `id` лучше использовать обёртку `Integer`, вместо примитивного типа `int`.
        // `Integer` (обёртка) может быть `null` и для нового объекта поле `id` будет `null` до тех пор, пока Hibernate не присвоит ему значение после сохранения.
        // А `int` (примитив) не может быть `null` и для нового, ещё не сохраненного объекта, поле `id` будет иметь значение по умолчанию `0`.
        // Использование обертки `Integer` является предпочтительным для генерируемых ID, потому что позволяет легко и надёжно определить,
        // является ли сущность новой, просто проверив `if (id == null)`.
        // С использованием примитива `0` может оказаться валидным значением ID (хотя и редко), что создаст путаницу.

    // Связи `@ManyToOne` не имеют явного указания о стратегии загрузки.
        // По умолчанию для `@ManyToOne` используется `FetchType.EAGER`, что приводит к немедленной загрузке связанных сущностей при загрузке `Match`.
        // Это может вызывать проблемы производительности (N+1 запросов) и излишнюю загрузку данных, особенно если связанные объекты не всегда нужны.

    // TODO: сеттеры не нужны — позволяют создать объект с установленным id или изменить состав игроков после создания
        // (например сделать постороннего игрока победителем).

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player1_id", nullable = false)
    private Player player1;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player2_id", nullable = false)
    private Player player2;

    @ManyToOne(optional = false)
    @JoinColumn(name = "winner_id", nullable = false)
    private Player winner;

    // Спецификация JPA требует наличия конструктора без аргументов для создания экземпляров сущностей,
        // однако ему не обязательно быть `public`. Когда конструктор публичный, он становится частью общедоступного API класса.
        // Это позволяет использовать его для создания "пустых", невалидных объектов (без установки обязательных полей)
        // в любом месте приложения, хотя он предназначен исключительно для внутреннего использования фреймворком (JPA).
        //
        // Хорошим подходом будет ограничить область видимости этого конструктора до `protected`.
        // Это делает его недоступным для прямого вызова из других пакетов, но оставляет видимым для JPA и дочерних классов.
    public Matches(){}

    public Matches(Player player1, Player player2, Player winner){
        this.player1 = player1;
        this.player2 = player2;
        this.winner = winner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}