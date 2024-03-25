package restApplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResponseDTO> generateExceptions(ValidationException ex) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTime(new Date().toString());
        for (String[] e : ex.errors) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setCause(e[0]);
            errorDTO.setMessage(e[1]);
            errorDTO.setTime(new Date().toString());
            responseDTO.addError(errorDTO);
        }
        return new ResponseEntity<>(responseDTO, HttpStatus.UNPROCESSABLE_ENTITY);
    }


}
