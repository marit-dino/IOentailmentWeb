import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {EntailmentComponent} from "./entailment/entailment.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    EntailmentComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}
