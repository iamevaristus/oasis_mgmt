import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, catchError, tap } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';
import { LoginRequest, SignupRequest, User } from '../models/user.model';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8080/api/v1/auth';
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(
      private http: HttpClient,
      private router: Router
  ) {
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.currentUserSubject.next(JSON.parse(storedUser));
    }
  }

  signup(request: SignupRequest): Observable<ApiResponse<User>> {
    return this.http.post(this.API_URL + '/signup', request, this.httpOptions).pipe(
        tap((res: any) => {
          const response = ApiResponse.fromJson<User>(res);

          if (response.isSuccessful) {
            localStorage.setItem('currentUser', JSON.stringify(response.data));
            this.currentUserSubject.next(response.data);
          } else {
            throw new Error(response.message);
          }
        }),
        catchError(error => {
          console.error('Signup error:', error);
          if (error.error && error.error.message) {
            throw new Error(error.error.message);
          } else if(error) {
            throw new Error(error);
          }

          throw new Error('An error occurred during signup. Please try again.');
        })
    );
  }

  login(request: LoginRequest): Observable<ApiResponse<User>> {
    return this.http.post(this.API_URL + '/login', request, this.httpOptions).pipe(
        tap((res: any) => {
          const response = ApiResponse.fromJson<User>(res);

          if (response.isSuccessful) {
            localStorage.setItem('currentUser', JSON.stringify(response.data));
            this.currentUserSubject.next(response.data);
          } else {
            throw new Error(response.message);
          }
        }),
        catchError(error => {
          console.error('Login error:', error);
          if (error.error && error.error.message) {
            throw new Error(error.error.message);
          } else if(error) {
            throw new Error(error);
          }

          throw new Error('An error occurred during login. Please try again.');
        })
    );
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    this.router.navigate(['/']).then(r => {});
  }

  get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  get isAuthenticated(): boolean {
    return !!this.currentUserValue;
  }
}