# IOentailment

This program implements the encoding of the entailment problemInput for Input/Output Logics $OUT_1$, $OUT_2$, $OUT_3$, $OUT_4$ and their causal counterparts $OUT_1^{\perp}$, $OUT_2^{\perp}$, $OUT_3^{\perp}$, $OUT_4^{\perp}$ in classical propositional logic. The encoded problem is then be efficiently solved by a SAT-Solver. The encoding is taken from [Streamlining Input/Output Logics with Sequent Calculi](https://proceedings.kr.org/2023/15/kr2023-0015-ciabattoni-et-al.pdf) by Ciabattoni and Rozplokhas.


### Requirements
Maven and the following dependencies are needed to compile the project.
- [Maven](https://maven.apache.org/) 
- [Z3](https://github.com/Z3Prover/z3/)
  (For Linux the LD_LIBRAY_PATH has to be exported: `export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/path/to/Z3/bin`.)


### Run
- To start the backend run `mvn spring-boot:run` in the _backend_ folder.
- To start the fronted run `ng serve` in the _frontend_ folder.

Then the application is available at http://localhost:4200/.
