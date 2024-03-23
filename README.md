# IOentailment

This program implements the encoding of the entailment problemInput for Input/Output Logics $OUT_1$, $OUT_2$, $OUT_3$, $OUT_4$ and their causal counterparts $OUT_1^{\perp}$, $OUT_2^{\perp}$, $OUT_3^{\perp}$, $OUT_4^{\perp}$ in classical propositional logic. The encoded problemInput is then be efficiently solved by a SAT-Solver. The encoding is taken from [Streamlining Input/Output Logics with Sequent Calculi](https://proceedings.kr.org/2023/15/kr2023-0015-ciabattoni-et-al.pdf) by Ciabattoni and Rozplokhas.


### Requirements
Maven and the following dependencies are needed to compile the project.
- [Maven](https://maven.apache.org/) 
- [jline3](https://github.com/jline/jline3)
- [javacc](https://javacc.github.io/javacc/)  
- [Z3](https://github.com/Z3Prover/z3/)

### Run
To compile and run the program enter `mvn compile exec:java`.
