
package ma.stock.configurations.handlers;


import ma.stock.utilities.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        List<Map<String, String>> errorsObj = errors.stream()
                .map(error -> {
                    Map<String, String> errorsMap = new HashMap<>();
                    errorsMap.put(error.getField(), error.getDefaultMessage());
                    return errorsMap;
                })
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(new CustomResponse<>("Error Accurate :(",errorsObj));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        System.err.println(ex.getMessage());   // TODO :: to delete
        ex.printStackTrace();// TODO :: to delete
        Map<String, String> errorsMap = new HashMap<>();
        errorsMap.put("error", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new CustomResponse<>("Error Accurate :(",List.of(errorsMap)));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentExceptions(IllegalArgumentException ex){
        Map<String, String> errorsMap = new HashMap<>();
        errorsMap.put("error", ex.getMessage());
        errorsMap.put("message","can't perform this operation");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CustomResponse<>("Error Accurate :(",List.of(errorsMap)));
    }
}
