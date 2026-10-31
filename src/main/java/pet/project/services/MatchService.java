package pet.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pet.project.dao.MatchDao;
import pet.project.dto.*;
import pet.project.entities.Matches;
import pet.project.entities.Player;
import pet.project.exceptions.PlayerNotFoundByNameException;
import pet.project.exceptions.SameNamePlayersException;
import pet.project.models.Match;
import pet.project.models.TennisPlayer;
import pet.project.storage.MatchStorage;
import pet.project.utils.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component // Для сервисов можно использовать специальную стереотипную аннотацию — @Service
public class MatchService {

    // Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // TODO: Класс совмещает несколько разных ответственностей:
        // - работает с текущими матчами
        // - работает с завершёнными матчами
        // - занимается валидацией данных из запроса
        // - занимается преобразованием в DTO
        //
        // Это нарушает принцип единой ответственности (SRP).
        // Стоит разделить эту логику на разные более специализированные классы.

    // Размер страницы по умолчанию более уместно хранить в контроллере, так как в идеале он должен приходить с фронтенда.
        // А сервис должен принимать это значение в качестве аргумента в методы.
    private static final int LIMIT_VALUE = 2; // Лучше DEFAULT_PAGE_SIZE

    private final PlayerService playerService;
    private final MatchStorage matchStorage;
    private final MatchDao matchDao;

    // Если конструктор единственный в классе, то @Autowired над ним можно не писать
    @Autowired
    public MatchService(PlayerService playerService, MatchDao matchDao) {
        this.playerService = playerService;
        this.matchDao = matchDao;

        // TODO: Сервис должен не создавать свои зависимости, а получать их через конструктор.
        this.matchStorage = new MatchStorage();
    }

    @Transactional
    public UUID createMatch(CreateMatchRequest createMatchRequest){
        Player firstPlayer = playerService.findOrCreatePlayer(createMatchRequest.getFirstPlayerName());
        Player secondPlayer = playerService.findOrCreatePlayer(createMatchRequest.getSecondPlayerName());

        // TODO: Валидировать входные данные нужно до выполнения любой логики и особенно до обращения в БД.
            // Чтобы не выполнять лишних запросов в базу, если данные не прошли валидацию.
        if(firstPlayer.equals(secondPlayer)){
            throw new SameNamePlayersException(firstPlayer.getName(), secondPlayer.getName());
        }

        TennisPlayer tennisFirstPlayer = new TennisPlayer(firstPlayer.getName());
        TennisPlayer tennisSecondPlayer = new TennisPlayer(secondPlayer.getName());

        UUID uuid = UUID.randomUUID();
        Match match = new Match(tennisFirstPlayer, tennisSecondPlayer);
        matchStorage.addMatch(uuid, match);

        return uuid;
    }

    // TODO: Race condition при обработке выигранного очка.
        // Например, если пользователь очень быстро нажмёт кнопку выигрыша очка, браузер отправит два POST-запроса почти одновременно.
        // Эти два запроса будут обработаны в двух разных потоках и оба потока будут работать с одним и тем же общим объектом `Match`,
        // что может привести к попытке начисления очка в уже завершённом матче:
            // Оба потока получают ссылку на объект матча — matchStorage.findMatchByUUID(uuid).
            // Первый поток добавляет решающее очко, isMatchCompleted становится true,
            // он сохраняет матч в БД.
            // Затем второй поток, который имеет уже устаревшую к этому моменту ссылку,
            // вызывает addPointTo() на уже завершённом матче.
        // Чтобы это исправить нужно сделать так, чтобы с одним и тем же матчем
            // на протяжении всей обработки очка работал только один поток.
    // TODO: Открывать транзакцию на каждое очко не нужно — это создаёт лишние накладные расходы.
        // Достаточно, чтобы в транзакции происходило только сохранение завершённого матча.
//    @Transactional
    public MatchDto addPoint(UUID uuid, AddPointRequest addPointRequest){
        Match currentMatch = matchStorage.findMatchByUUID(uuid);
        currentMatch.addPointTo(addPointRequest.getName());

        PlayerDto firstPlayerDto = playerService.playerToPlayerDto(currentMatch.getFirstPlayer().name(), currentMatch);
        PlayerDto secondPlayerDto = playerService.playerToPlayerDto(currentMatch.getSecondPlayer().name(), currentMatch);

        // Если матч завершён, то нужно сразу создавать DTO с именем победителя.
        MatchDto matchDto = new MatchDto(firstPlayerDto, secondPlayerDto, null);

        if(currentMatch.isMatchCompleted()){

            // Использование `System.out` для логирования — плохая практика в серверных приложениях.
                // Логи должны выводиться через специализированный фреймворк.
            System.out.println(getClass().getName());
            saveFinishedMatch(currentMatch, matchDto);
        }

        return matchDto;
    }

    // Метод, который не является частью публичного API класса, не должен быть public.
    // TODO: Метод не только сохраняет завершённый матч, но и мутирует переданный объект (устанавливает победителя в MatchDto).
        // Это является скрытым побочным эффектом и нарушает Принцип наименьшего удивления.
        // (см. файл "pola.md" в этом же пакете)
    // TODO: Self-invocation метода с @Transactional.
        // Spring реализует декларативные транзакции с помощью AOP и прокси-объектов.
        // Если метод methodA() внутри сервиса вызывает другой метод methodB(), помеченный как @Transactional,
        // этот вызов происходит напрямую через обычный this.methodB(), минуя прокси-объект.
        // Поэтому прокси не видит этот вызов и код выполняется просто как обычный Java-метод без транзакции.
    @Transactional
    public void saveFinishedMatch(Match currentMatch, MatchDto matchDto){
        String winnerName = currentMatch.getWinner().name();
        matchDto.setWinnerName(winnerName);

        Player firstPlayer = playerService.findByName(currentMatch.getFirstPlayer().name());
        Player secondPlayer = playerService.findByName(currentMatch.getSecondPlayer().name());

        // Проверить совпадение имён можно без утилитного класса Objects.
        Player winnerPlayer = Objects.equals(winnerName, firstPlayer.getName()) ? firstPlayer : secondPlayer;

        Matches matches = new Matches(firstPlayer, secondPlayer, winnerPlayer);

        matchDao.save(matches);
    }

    @Transactional(readOnly = true)
    public ListFinishedMatchDto getFinishedMatches(Integer page, String playerName){

        page = Validator.validateIntegerPage(page);

        if(playerName != null && !playerService.existsByName(playerName)){

            // Если по поисковому запросу ничего не найдено, вместо ошибки можно показывать пустую страницу.
            throw new PlayerNotFoundByNameException(playerName);
        }

        int offset = page * LIMIT_VALUE;

        List<Matches> listMatches = (playerName == null) ? matchDao.findFinishedMatches(LIMIT_VALUE, offset)
                : matchDao.findFinishedMatchesByPlayerName(LIMIT_VALUE, offset, playerName);

        Long totalRecords = (playerName == null) ? matchDao.countByFindFinishedMatches()
                : matchDao.countByFindFinishedMatchesByPlayerName(playerName);

        return listMatchesToDto(listMatches, page, totalRecords);
    }

    // Лучше mapMatchesToDto
    private ListFinishedMatchDto listMatchesToDto(List<Matches> listMatches, int page, long totalRecords){
        ListFinishedMatchDto listFinishedMatchDto = new ListFinishedMatchDto();

        // Лучше будет читаться, если сначала создать заполненный объектами List, а затем уже передать его в DTO.
        listFinishedMatchDto.setMatches(new ArrayList<>());

        for(Matches matches : listMatches){
            FinishedMatchDto finishedMatchDto = new FinishedMatchDto();
            finishedMatchDto.setFirstPlayerName(matches.getPlayer1().getName());
            finishedMatchDto.setSecondPlayerName(matches.getPlayer2().getName());
            finishedMatchDto.setWinnerName(matches.getWinner().getName());

            listFinishedMatchDto.getMatches().add(finishedMatchDto);
        }

        listFinishedMatchDto.setCurrentPage(page);

        // Можно так: long totalPages = (totalRecords + LIMIT_VALUE - 1) / LIMIT_VALUE;
        double totalPagesDouble = (totalRecords * 1.0) / LIMIT_VALUE;
        long totalPages = (long) Math.ceil(totalPagesDouble);

        listFinishedMatchDto.setTotalPages(totalPages);

        // Логика создания ListFinishedMatchDto раздроблена. Стоит сначала подготовить все необходимые данные,
            // а в конце метода создать DTO, передав в конструктор сразу всё необходимое.
        return listFinishedMatchDto;
    }

    public MatchDto getScore(UUID uuid){
        Match currentMatch = matchStorage.findMatchByUUID(uuid);

        PlayerDto firstPlayerDto = playerService.playerToPlayerDto(currentMatch.getFirstPlayer().name(), currentMatch);
        PlayerDto secondPlayerDto = playerService.playerToPlayerDto(currentMatch.getSecondPlayer().name(), currentMatch);

        // Если матч завершён, то нужно сразу создавать DTO с именем победителя.
        MatchDto matchDto = new MatchDto(firstPlayerDto, secondPlayerDto, null);

        if(currentMatch.isMatchCompleted()){
            matchDto.setWinnerName(currentMatch.getWinner().name());
        }

        return matchDto;
    }
}
