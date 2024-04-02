import {Component, Input, model, OnInit} from '@angular/core';
import {Response} from "../inteface/response";
import {CommonModule, KeyValue} from "@angular/common";

@Component({
  selector: 'app-countermodel',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './countermodel.component.html',
  styleUrl: './countermodel.component.css'
})
export class CountermodelComponent {
  @Input() response!: Response;
  modelW?:Map<string, Map<string, Map<string, boolean>>>;
  modelC?: Map<string, Map<string, boolean>>;

  ngOnInit() {
    if(this.response.counterModelC != null){
      this.modelC = this.response.counterModelC;
    }
    if(this.response.counterModelW != null){
      this.modelW = this.response.counterModelW;
    }
  }




}
