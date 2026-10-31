package pet.project.dto;

import pet.project.enums.Point;

public class PlayerDto {

    // Этот класс можно сделать record-ом. Это значительно сократит шаблонный код.

    // Точнее было бы назвать PlayerScoreDto

    // В объектах DTO лучше не иметь сеттеров без крайней необходимости (которой в этом проекте нет),
        // чтобы не было возможности забыть установить одно из полей.

    private String name;
    private String points;
    private int games;
    private int sets;
    private Integer tieBreakPoints;

    public PlayerDto(){}

    public PlayerDto(String name, String points, int games, int sets, Integer tieBreakPoints) {
        this.name = name;
        this.points = points;
        this.games = games;
        this.sets = sets;
        this.tieBreakPoints = tieBreakPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public Integer getTieBreakPoints() {
        return tieBreakPoints;
    }

    public void setTieBreakPoints(Integer tieBreakPoints) {
        this.tieBreakPoints = tieBreakPoints;
    }
}
