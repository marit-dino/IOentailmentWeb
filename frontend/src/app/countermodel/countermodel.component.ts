import {Component, Input, OnInit} from '@angular/core';
import {Response} from "../inteface/response";
import {CommonModule} from "@angular/common";

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
  modelW?: Map<string, Map<number, boolean>> = undefined;
  modelC?: Map<string,boolean> = undefined;

  ngOnInit() {
    console.log(this.response);
    console.log(this.response.counterModelWorlds)
    console.log(this.response.counterModelClassical)
    if(this.response.counterModelClassical != undefined){
      this.modelC = this.response.counterModelClassical;
    }
    if(this.response.counterModelWorlds != undefined){
      this.modelW = this.response.counterModelWorlds;
    }
  }

}
