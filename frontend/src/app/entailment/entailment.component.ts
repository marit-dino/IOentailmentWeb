import {Component, inject} from '@angular/core';
import {CommonModule, NgIf} from '@angular/common';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { FormBuilder } from '@angular/forms';
import {Problem} from "../inteface/problem";
import {Error} from "../inteface/error";
import {EntailmentService} from "../service/entailment.service";
import {animate, style, transition, trigger} from "@angular/animations";
import {Response} from "../inteface/response";
import {CountermodelComponent} from "../countermodel/countermodel.component";

@Component({
  selector: 'app-entailment',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
    CountermodelComponent,
  ],
  templateUrl: './entailment.component.html',
  styleUrl: './entailment.component.css',
  providers: [EntailmentService],
  animations: [
    trigger('trigger', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('250ms', style({ opacity: 1 })),
      ]),
      transition(':leave', [
        animate('0ms', style({ opacity: 0 }))
      ])
    ]),
  ]
})
export class EntailmentComponent {
  private entailmentService =  inject(EntailmentService);
  problem: Problem = {
    goalPair: "",
    derivingPairs: "",
    type: ""
  }
  submitted = false;

  public errorMessageGP = "";
  public errorMessageDP = "";
  public errorMessageLogic = "";
  public serverNotReachable = false;
  response?: Response = undefined;

  inputForm : FormGroup = new FormGroup({
    derivingPairs: new FormControl<string>(this.problem.derivingPairs),
    goalPair: new FormControl<string>(this.problem.goalPair, [Validators.required]),
    type: new FormControl<string>(this.problem.type, [Validators.required]),
    });


  constructor(private formBuilder: FormBuilder) {}

  sendProblem(instance: Problem) {
    this.serverNotReachable = false;
    this.problem = instance;
    this.submitted = true;
    this.errorMessageDP = "";
    this.errorMessageGP = "";
    this.errorMessageLogic = "";
    this.response = undefined;

    this.entailmentService.sendProblem(this.problem)
      .subscribe({
        next: data => {
          this.response = data;
        },
        error: error => {
          if(error.error == null) {
            this.serverNotReachable = true;
          }
          else {
            let arr: Error[] = error.error.errors;
            if(arr == undefined){
              this.serverNotReachable = true;
            }
            for (let i = 0; i < arr.length; i++) {
              if (arr[i].cause == "GoalPairParseException") {
                this.errorMessageGP += ((this.errorMessageGP.length > 0) ? '\n' : '') + arr[i].message;
              }
              if (arr[i].cause == "DerivingPairsParseException") {
                this.errorMessageDP += ((this.errorMessageDP.length > 0) ? '\n' : '') + arr[i].message;
              }
              if (arr[i].cause == "IllegalLogicException") {
                this.errorMessageLogic += ((this.errorMessageLogic.length > 0) ? '\n' : '') + arr[i].message;
              }
            }
          }
        }
      });
  }



}
