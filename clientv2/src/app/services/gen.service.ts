import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Gen } from '../models/gen';


@Injectable({
  providedIn: 'root'
})
export class GenService {

  API_URL = "http://localhost:8080/gen-loader/gene" 

  constructor(private http: HttpClient) { }

  test(): Observable<any>{
    return this.http.get(`http://localhost:8080/gen-loader/gene/check2/JULIO`);

  }

  insertGen(idGen: string): Observable<any>{
    return this.http.post(`http://localhost:8080/gen-loader/gene/test/${idGen}`, idGen);
  }

  getGenes(): Observable<Gen[]>{
    return this.http.get<Gen[]>(`http://localhost:8080/gen-loader/gene/genes`);
  }

  truncate(): Observable<any>{
    console.warn('eliminando....');
    return this.http.post(`http://localhost:8080/gen-loader/gene/truncate`, '');
  }

}
