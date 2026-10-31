package pet.project.storage;

import pet.project.exceptions.MatchNotFoundException;
import pet.project.models.Match;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MatchStorage {

    // TODO: Класс не является Spring-бином. Это лишает проект преимуществ Spring

    // TODO: Нет метода удаления завершённых матчей из хранилища.
        // При долгой работе приложения это может привести к исчерпанию памяти.

    private ConcurrentMap<UUID, Match> matches; // Это поле стоит сделать final.

    public MatchStorage(){
        matches = new ConcurrentHashMap<>();
    }

    // Хранилище может само создавать ID для матча (по аналогии с БД) и возвращать его из этого метода.
    public void addMatch(UUID uuid, Match match){
        this.matches.put(uuid, match);
    }

    public Match findMatchByUUID(UUID uuid){
        Match match = this.matches.get(uuid);
        if(match == null){
            throw new MatchNotFoundException(uuid);
        }
        return match;
    }

    // TODO: Метод отдаёт наружу внутреннюю Map класса.
        // Это позволяет любому внешнему коду бесконтрольно её изменить. Например, удалить все матчи.
    public ConcurrentMap<UUID, Match> getMatches() {
        return matches;
    }

    // TODO: Внутреннее хранилище матчей должно быть неизменным.
        // Сейчас любой внешний код может поменять внутреннее хранилище в любой момент времени.
    public void setMatches(ConcurrentHashMap<UUID, Match> matches) {
        this.matches = matches;
    }
}
