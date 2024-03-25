package restApplication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import restApplication.exceptions.ValidationException;

@RestController
public class Controller {
    //TODO logger

    @RequestMapping("/")
    String home() {
        return "Hello World :)";
    }

    /**
     * Takes the user input and returns the solution of the entailment problem.
     *
     * @param problemInput fields of the entailment problem
     * @return whether deriving pairs entail goal pair or not
     */
    @PostMapping("/entailment")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public boolean postEntailmentProblem(@RequestBody ProblemInput problemInput) throws ValidationException {
            return ProblemSolver.solveProblem(problemInput);
    }


}