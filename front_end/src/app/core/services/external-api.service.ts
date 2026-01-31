import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, of } from 'rxjs';
import { Destination } from '../models/models';

@Injectable({
    providedIn: 'root'
})
export class ExternalApiService {
    constructor(private http: HttpClient) { }

    private baseUrl = 'https://restcountries.com/v3.1';
    private fields = 'name,capital,region,population,currencies,flags';

    getCountrySuggestions(): Observable<Destination[]> {
        return this.http.get<any[]>(`${this.baseUrl}/all?fields=${this.fields}`).pipe(
            map(countries => this.mapCountries(countries))
        );
    }

    searchByName(name: string): Observable<Destination[]> {
        if (!name.trim()) return of([]);
        return this.http.get<any[]>(`${this.baseUrl}/name/${name}?fields=${this.fields}`).pipe(
            map(countries => this.mapCountries(countries))
        );
    }

    private mapCountries(countries: any[]): Destination[] {
        return countries.map(c => ({
            country: c.name.common,
            capital: c.capital && c.capital.length > 0 ? c.capital[0] : 'N/A',
            region: c.region,
            population: c.population,
            currency: c.currencies ? Object.keys(c.currencies)[0] : 'N/A',
            flagUrl: c.flags.svg || c.flags.png
        }));
    }
}
