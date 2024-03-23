package restApplication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;
import parser.ParseException;
import restApplication.exceptions.DerivingPairsParseException;
import restApplication.exceptions.GoalPairParseException;
import restApplication.exceptions.IllegalLogicException;

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
    public boolean postEntailmentProblem(
            @RequestBody ProblemInput problemInput) {
        try{
            return ProblemSolver.solveProblem(problemInput);
        }
        catch(GoalPairParseException | DerivingPairsParseException | IllegalLogicException e){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }


}