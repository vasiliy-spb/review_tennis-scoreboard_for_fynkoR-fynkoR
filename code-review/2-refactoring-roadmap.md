# Роадмап рефакторинга по файлам

Это упорядоченный список файлов, которые следует исправлять в соответствии с замечаниями в комментариях. Рекомендую двигаться последовательно.

Файлы, не указанные в списке, можно исправлять в любом порядке.

### Шаг 1: Entity и слой доступа к данным

- `/entities/Player.java`
- `/entities/Matches.java`
- `/dao/PlayerDao.java`
- `/dao/MatchDao.java`

### Шаг 2: Доменные модели

- `/models/Game.java`
- `/models/TieBreak.java`
- `/models/Set.java`
- `/models/Match.java`

### Шаг 3: Сервисный слой

- `/storage/MatchStorage.java`
- `/services/PlayerService.java`
- `/services/MatchService.java`

### Шаг 4: DTO (Data Transfer Object)

- `/dto/*`

### Шаг 5: Контроллер

- `/controllers/MatchController.java`

### Шаг 6: Конфигурация, мапперы, валидаторы, обработка исключений

- `/configs/WebConfig.java`
- `/exceptions/GlobalExceptionHandler.java`
- `/utils/Validator.java`

### Шаг 7: Тесты

- `/test/java/pet/project/enums/PointTest.java`
- `/test/java/pet/project/models/GameTest.java`
- `/test/java/pet/project/models/TieBreakTest.java`
- `/test/java/pet/project/models/SetTest.java`
- `/test/java/pet/project/models/MatchTest.java`
