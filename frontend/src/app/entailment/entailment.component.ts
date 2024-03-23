import {Component, inject} from '@angular/core';
import {NgIf} from '@angular/common';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { FormBuilder } from '@angular/forms';
import { Problem} from "../inteface/problem";
import {EntailmentService} from "../service/entailment.service";
import { KatexModule } from "ng-katex";

@Component({
  selector: 'app-entailment',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    KatexModule
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
    type: ""
  }
  submitted = false;

  private errorMessage = "";
  entails?: boolean = undefined;

  inputForm : FormGroup = new FormGroup({
    derivingPairs: new FormControl<string>(this.problem.derivingPairs),
    goalPair: new FormControl<string>(this.problem.goalPair, [Validators.required]),
    type: new FormControl<string>(this.problem.type, [Validators.required]),
    });


  constructor(private formBuilder: FormBuilder) {}

  sendProblem(instance: Problem) {
    this.problem = instance;
    console.log(this.problem);
    this.submitted = true;
    this.entailmentService.sendProblem(this.problem)
      .subscribe({
        next: data => {
          this.entails = data.valueOf();
        },
        error: error => {
        this.errorMessage = error.message;
        console.error('There was an error!', error);
       }
      });
  }



}
