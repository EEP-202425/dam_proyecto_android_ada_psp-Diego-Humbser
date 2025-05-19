package dam.proyectofinal.mireparto.exception;

import java.util.List;
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
	
}
