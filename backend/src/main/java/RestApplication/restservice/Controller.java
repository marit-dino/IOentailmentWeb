package RestApplication.restservice;

import RestApplication.Entity.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import parser.ParseException;

import RestApplication.application.ProblemSolver;

@RestController
public class Controller {
    //TODO logger

    @RequestMapping("/")
    String home() {
        return "Hello World :)";
    }

    @PostMapping("/entailment")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public boolean postEntailmentProblem(
            @RequestBody Problem problem) {
        System.out.println(problem);
        try {
            return ProblemSolver.solveProblem(problem);
        }
        catch (ParseException e) {
            //TODO
        }
        return false;
    }


}