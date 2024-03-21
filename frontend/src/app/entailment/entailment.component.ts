import {Component, inject} from '@angular/core';
import {CommonModule, NgIf} from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Problem} from "../inteface/problem";
import {EntailmentService} from "../service/entailment.service";

@Component({
  selector: 'app-entailment',
  standalone: true,
  imports: [
    FormsModule,
    NgIf
  ],
  templateUrl: './entailment.component.html',
  styleUrl: './entailment.component.css',
  providers: [EntailmentService]
})
export class EntailmentComponent {
  private entailmentService =  inject(EntailmentService);

  problem: Problem = {
    goalPair: "",
    derivingPairs: "",
    type: "",
    entails: undefined
  }

  sendProblem() {
    this.problem.entails = undefined;
    this.entailmentService.sendProblem(this.problem)
      .subscribe(data => this.problem.entails = data.valueOf())
  }
}
