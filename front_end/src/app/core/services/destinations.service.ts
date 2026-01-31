import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Destination, PaginatedResponse, ApiResponse } from '../models/models';
import { map } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class DestinationsService {
    private apiUrl = `${environment.apiUrl}/destinations`.replace(/([^:]\/)\/+/g, "$1");

    constructor(private http: HttpClient) { }

    getDestinations(keyword: string = '', page: number = 0, size: number = 10): Observable<PaginatedResponse<Destination>> {
        let params = new HttpParams()
            .set('keyword', keyword)
            .set('page', page.toString())
            .set('size', size.toString());
        return this.http.get<ApiResponse<PaginatedResponse<Destination>>>(this.apiUrl, { params }).pipe(
            map(res => res.data)
        );
    }

    bulkCreate(destinations: Destination[]): Observable<any> {
        return this.http.post(this.apiUrl, destinations);
    }

    bulkDelete(ids: number[]): Observable<any> {
        return this.http.delete(this.apiUrl, { body: ids });
    }

    getWantToVisit(keyword: string = '', page: number = 0, size: number = 10): Observable<PaginatedResponse<Destination>> {
        let params = new HttpParams()
            .set('keyword', keyword)
            .set('page', page.toString())
            .set('size', size.toString());
        return this.http.get<ApiResponse<PaginatedResponse<Destination>>>(`${this.apiUrl}/want-to-visit`, { params }).pipe(
            map(res => res.data)
        );
    }

    markWantToVisit(ids: number[]): Observable<any> {
        return this.http.post(`${this.apiUrl}/want-to-visit`, ids);
    }

    unmarkWantToVisit(ids: number[]): Observable<any> {
        return this.http.post(`${this.apiUrl}/unwant-to-visit`, ids);
    }
}
