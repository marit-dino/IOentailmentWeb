package restApplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice
public class CustomExceptionHandler {

    /**
     * Handles ValidationException and sends all occurred errors to the client.
     * @param ex Validation Exception
     * @return a ResponseEntity with a representation of the errors°
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDTO> generateExceptions(ValidationException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setTime(new Date().toString());
        for (String[] e : ex.errors) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setCause(e[0]);
            errorDTO.setMessage(e[1]);
            errorDTO.setTime(new Date().toString());
            errorResponseDTO.addError(errorDTO);
        }
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.UNPROCESSABLE_ENTITY);
    }


}
