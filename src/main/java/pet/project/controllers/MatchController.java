package pet.project.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pet.project.dto.*;
import pet.project.services.MatchService;

import java.util.UUID;

@Controller // Для REST контроллера есть специальная аннотация @RestController
@RequestMapping("/matches")
public class MatchController {

    private final MatchService matchService;

    // Если конструктор единственный в классе, то @Autowired над ним можно не писать
    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping
    public ResponseEntity<CreateMatchResponse> createMatch(@Valid @RequestBody CreateMatchRequest createMatchRequest){
        UUID uuid = matchService.createMatch(createMatchRequest);

        // Вместо ручной установки Location лучше построить путь к созданному ресурсу с учётом контекста
            // (например, через MvcUriComponentsBuilder).
            // Так путь всегда будет корректным, даже если приложение развёрнуто не в корне.
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/matches/" + uuid)
                .body(new CreateMatchResponse(uuid));
    }

    // TODO: Нет валидации @RequestBody AddPointRequest.
        // Без неё при некорректном DTO вместо ошибки 400, клиент получит 500 от исключения позже.
    @PostMapping("/{uuid}/point")
    public ResponseEntity<MatchDto> addPoint(@RequestBody AddPointRequest addPointRequest, @PathVariable("uuid") UUID uuid){
        MatchDto matchDto = matchService.addPoint(uuid, addPointRequest);
        return ResponseEntity.ok(matchDto);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<MatchDto> getScore(@PathVariable("uuid") UUID uuid){
        MatchDto matchDto = matchService.getScore(uuid);
        return ResponseEntity.ok(matchDto);
    }

    @GetMapping
    public ResponseEntity<ListFinishedMatchDto> getFinishedMatches(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                                   @RequestParam(name = "player_name", required = false) String playerName){
        ListFinishedMatchDto listFinishedMatchDto = matchService.getFinishedMatches(page - 1, playerName);

        // TODO: CurrentPage уже устанавливается в сервисе и не должна перетираться здесь другим значением.
            // Это создаёт несогласованность: 0-based в сервисе vs 1-based в контроллере.
        listFinishedMatchDto.setCurrentPage(page);
        return ResponseEntity.ok(listFinishedMatchDto);
    }

}
