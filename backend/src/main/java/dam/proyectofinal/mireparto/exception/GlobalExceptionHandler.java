package dam.proyectofinal.mireparto.exception;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

public class GlobalExceptionHandler {

    // 400 Bad Request por validación @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        List<ErrorResponse.ValidationError> errors = ex.getBindingResult().getFieldErrors()
            .stream()
            .map(fe -> {
                ErrorResponse.ValidationError ve = new ErrorResponse.ValidationError();
                ve.setField(fe.getField());
                ve.setMessage(fe.getDefaultMessage());
                return ve;
            })
            .collect(Collectors.toList());

        ErrorResponse body = new ErrorResponse();
        body.setStatus(HttpStatus.BAD_REQUEST.value());
        body.setError("Bad Request");
        body.setErrors(errors);
        body.setPath(request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    
    // 404 Not Found para recursos no existentes
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            NoSuchElementException ex,
            WebRequest request) {

        ErrorResponse body = new ErrorResponse();
        body.setStatus(HttpStatus.NOT_FOUND.value());
        body.setError(ex.getMessage());
        body.setPath(request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
    
    // 500 Internal Server Error para excepciones no controladas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(
            Exception ex,
            WebRequest request) {

        ErrorResponse body = new ErrorResponse();
        body.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.setError("Internal server error");
        body.setPath(request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
}
