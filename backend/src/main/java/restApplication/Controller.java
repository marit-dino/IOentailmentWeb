package restApplication;

import encoding.EntailmentProblem;
import encoding.IOLogics.CounterModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import restApplication.exceptions.ValidationException;

@RestController
public class Controller {
    private static final Logger logger = LogManager.getLogger();

    /**
     * Takes the user input and returns the solution of the entailment problem.
     *
     * @param problemInput fields of the entailment problem
     * @throws ValidationException when parsing fails
     * @return whether deriving pairs entail goal pair or not
     */
    @PostMapping("/entailment")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseDTO> postEntailmentProblem(@RequestBody ProblemInput problemInput) throws ValidationException {
        logger.info("POST /entailment {}", problemInput);
        EntailmentProblem p = ProblemTransformer.getInput(problemInput);
        boolean entails = p.entails();
        CounterModel counterModel = p.getCounterModel();
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setEntails(entails);
        responseDTO.setCounterModel(counterModel);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }


}