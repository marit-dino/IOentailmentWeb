package restApplication;

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
    public boolean postEntailmentProblem(@RequestBody ProblemInput problemInput) throws ValidationException {
        logger.info("POST /entailment");
        return ProblemSolver.solveProblem(problemInput);
    }


}