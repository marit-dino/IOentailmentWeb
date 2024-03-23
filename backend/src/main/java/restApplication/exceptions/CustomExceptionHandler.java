package restApplication.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class CustomExceptionHandler {

    //TODO send more than one error
    @ExceptionHandler(IllegalLogicException.class)
    public ResponseEntity<ErrorDTO> generateIllegalLogicException(IllegalLogicException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCause(ex.getClass().getSimpleName());
        errorDTO.setMessage(ex.getMessage());
        errorDTO.setStatus(String.valueOf(ex.getHTTPStatus().value()));
        errorDTO.setTime(new Date().toString());

        return new ResponseEntity<>(errorDTO, ex.getHTTPStatus());
    }

    @ExceptionHandler(GoalPairParseException.class)
    public ResponseEntity<ErrorDTO> generateGoalPairParseException(GoalPairParseException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCause(ex.getClass().getSimpleName());
        errorDTO.setMessage(ex.getMessage());
        errorDTO.setStatus(String.valueOf(ex.getHTTPStatus().value()));
        errorDTO.setTime(new Date().toString());

        return new ResponseEntity<>(errorDTO, ex.getHTTPStatus());
    }

    @ExceptionHandler(DerivingPairsParseException.class)
    public ResponseEntity<ErrorDTO> generateDerivingPairsParseException(DerivingPairsParseException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(ex.getMessage());
        errorDTO.setCause(ex.getClass().getSimpleName());
        errorDTO.setStatus(String.valueOf(ex.getHTTPStatus().value()));
        errorDTO.setTime(new Date().toString());

        return new ResponseEntity<>(errorDTO, ex.getHTTPStatus());
    }
}
