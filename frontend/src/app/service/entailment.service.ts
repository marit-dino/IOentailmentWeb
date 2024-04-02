import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {Problem} from "../inteface/problem";
import {catchError, Observable, throwError} from "rxjs";
import {Response} from "../inteface/response";

@Injectable({
  providedIn: 'root'
})
export class EntailmentService {
  entailmentUrl = '/entailment';

  constructor(private http: HttpClient) { }

  sendProblem(problem: Problem): Observable<Response> {
    return this.http.post<Response>(this.entailmentUrl, problem);
  }


}
