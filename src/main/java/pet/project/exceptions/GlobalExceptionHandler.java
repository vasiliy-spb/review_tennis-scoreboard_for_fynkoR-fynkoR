package pet.project.exceptions;

import jakarta.persistence.PersistenceException;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pet.project.dto.ErrorResponse;
import pet.project.utils.Validator;

@ControllerAdvice // Для REST перехватчика есть специальная аннотация @RestControllerAdvice
public class GlobalExceptionHandler {

    // TODO: Класс отправляет сообщение из исключения (`exception.getMessage()`) напрямую пользователю.
        // Сообщения об ошибках из исключений могут содержать технические детали, которые не предназначены
        // для конечного пользователя и могут представлять угрозу безопасности. Например, сообщение может быть
        // `"No entity found for query 'SELECT ...'"` или `"Validation failed for field 'internalFieldName'"`,
        // что раскрывает структуру БД или внутренние имена полей.
        //
        // Лучше никогда не отправлять необработанное сообщение из исключения на клиент.
        // Вместо этого можно использовать заранее определённые, безопасные сообщения или коды ошибок.
        // Само исключение при этом нужно логировать для разработчиков.
        //
        // Это повысит безопасность приложения и улучшит пользовательский опыт при возникновении ошибок.
        //
        // В этом проекте допустимо отправлять exception.getMessage() для ошибок валидации.

    // TODO: Набор обрабатываемых исключений неполный.
        // Стоит добавить обработку и других возможных исключений, в том числе общий обработчик
        // для всех непредвиденных ошибок (Exception), чтобы клиент никогда не получал stack trace.

    // TODO: Класс зависит от конкретной технологии персистентности (Hibernate).
        // Стоит использовать специализированную трансляцию исключений, которую предоставляет Spring
        // и перехватывать здесь именно его исключения.

    @ExceptionHandler(SameNamePlayersException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(SameNamePlayersException exception){
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(MatchNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundMatchException(MatchNotFoundException exception){
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(MatchIsAlreadyCompletedException.class)
    public ResponseEntity<ErrorResponse> handleMatchIsAlreadyCompletedException(MatchIsAlreadyCompletedException exception){
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(PlayerNotFoundByNameException.class)
    public ResponseEntity<ErrorResponse> handlePlayerNotFoundByNameException(PlayerNotFoundByNameException exception){
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException exception){
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception){
        String errorMessage = exception.getBindingResult()
                .getFieldErrors()
                .get(0) // Можно возвращать все ошибки, а не только первую, чтобы пользователь сразу мог исправить все недочёты
                .getDefaultMessage();
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(HibernateException.class)
    public ResponseEntity<ErrorResponse> handleHibernateException(HibernateException exception){
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ErrorResponse> handlePersistenceException(PersistenceException exception){
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

}
