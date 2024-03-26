import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {Problem} from "../inteface/problem";
import {catchError, Observable, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class EntailmentService {
  entailmentUrl = '/entailment';

  constructor(private http: HttpClient) { }

  sendProblem(problem: Problem): Observable<boolean> {
    return this.http.post<boolean>(this.entailmentUrl, problem);
  }


}
