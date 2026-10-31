package pet.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pet.project.dao.PlayerDao;
import pet.project.dto.PlayerDto;
import pet.project.entities.Player;
import pet.project.enums.PlayerPosition;
import pet.project.exceptions.PlayerNotFoundByNameException;
import pet.project.models.Game;
import pet.project.models.Match;
import pet.project.models.Scorable;
import pet.project.models.TieBreak;

import java.util.Optional;

@Component // Для сервисов можно использовать специальную стереотипную аннотацию — @Service
public class PlayerService {

    // Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // TODO: Класс совмещает несколько разных ответственностей:
        // - работает с репозиторием игроков
        // - работает с текущими матчами и занимается преобразованием счёта в DTO
        //
        // Это нарушает принцип единой ответственности (SRP).
        // Стоит разделить вынести логику преобразования в отдельный компонент — маппер.

    private final PlayerDao playerDao;

    // Если конструктор единственный в классе, то @Autowired над ним можно не писать
    @Autowired
    public PlayerService(PlayerDao playerDao){
        this.playerDao = playerDao;
    }

    @Transactional(readOnly = true)
    public Player findByName(String name){
        return playerDao.findByName(name).orElseThrow(() -> new PlayerNotFoundByNameException(name));
    }

    @Transactional(readOnly = true)
    public boolean existsByName(String name){

        // Для проверки существования можно создать специальный метод в DAO, чтобы не загружать полный объект из БД.
        return playerDao.findByName(name).isPresent();
    }

    @Transactional
    public Player findOrCreatePlayer(String name){
        Optional<Player> player = playerDao.findByName(name);
        if(player.isPresent()){
            return player.get();
        }
        else{
            Player playerNew = new Player(name);
            playerDao.save(playerNew);
            return playerNew;
        }
    }

    // Логика преобразования счёта больше уместна в маппере.
    public PlayerDto playerToPlayerDto(String name, Match match){

        // В проекте есть класс Player, поэтому "чистые" названия player лучше оставить для объектов именно этого типа,
            // а для PlayerDto использовать названия с префиксом Dto.
        PlayerDto player = new PlayerDto();
        player.setName(name);

        Scorable scorable = match.getSet().getCurrentGame();

        PlayerPosition playerPosition = match.isFirstPlayer(name);
        boolean isFirstPlayer = playerPosition == PlayerPosition.FIRST;

        if(scorable instanceof Game game){
            String points = isFirstPlayer ? game.getFirstPlayerPoint().getTitle() : game.getSecondPlayerPoint().getTitle();
            player.setPoints(points);
        }
        else {
            TieBreak tieBreak = (TieBreak) scorable;
            Integer points = isFirstPlayer ? tieBreak.getCounterFirstPlayer() : tieBreak.getCounterSecondPlayer();
            player.setTieBreakPoints(points);
        }

        player.setGames(isFirstPlayer ? match.getSet().getCounterFirstPlayer() : match.getSet().getCounterSecondPlayer());
        player.setSets(isFirstPlayer ? match.getCounterFirstPlayer() : match.getCounterSecondPlayer());

        return player;
    }
}
