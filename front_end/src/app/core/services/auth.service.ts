import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse, User, ApiResponse } from '../models/models';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private currentUserSubject = new BehaviorSubject<User | null>(null);
    public currentUser$ = this.currentUserSubject.asObservable();

    constructor(private http: HttpClient) {
        const savedUser = localStorage.getItem('user');
        if (savedUser) {
            this.currentUserSubject.next(JSON.parse(savedUser));
        }
    }

    login(credentials: any): Observable<User> {
        const url = `${environment.apiUrl}/auth/login`.replace(/([^:]\/)\/+/g, "$1");
        return this.http.post<ApiResponse<User>>(url, credentials).pipe(
            map(res => res.data),
            tap(user => this.setUser(user))
        );
    }

    register(userData: any): Observable<User> {
        const url = `${environment.apiUrl}/auth/register`.replace(/([^:]\/)\/+/g, "$1");
        return this.http.post<ApiResponse<User>>(url, userData).pipe(
            map(res => res.data),
            tap(user => this.setUser(user))
        );
    }

    logout() {
        localStorage.removeItem('user');
        this.currentUserSubject.next(null);
    }

    private setUser(user: User) {
        if (user) {
            localStorage.setItem('user', JSON.stringify(user));
            this.currentUserSubject.next(user);
        }
    }

    get currentUserValue(): User | null {
        return this.currentUserSubject.value;
    }

    get isLoggedIn(): boolean {
        return !!this.currentUserValue;
    }

    get isAdmin(): boolean {
        return this.currentUserValue?.role === 'ADMIN';
    }
}
