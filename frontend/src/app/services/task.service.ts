import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ApiResponse } from '../models/api-response.model';
import { CreateTaskRequest, Task, TaskCategory } from '../models/task.model';

@Injectable({
    providedIn: 'root'
})
export class TaskService {
    private readonly API_URL = 'http://localhost:8080/api/v1/task';

    constructor(private http: HttpClient) {}

    getAllTasks(): Observable<TaskCategory[]> {
        return this.http.get(this.API_URL).pipe(
            map((res: any) => {
                const response = ApiResponse.fromJson<TaskCategory[]>(res);
                if (!response.isSuccessful) {
                    throw new Error(response.message);
                }

                return response.data;
            }),
            catchError(error => {
                return throwError(() => new Error(`Error fetching tasks: ${error.message}`));
            })
        );
    }

    createTask(task: CreateTaskRequest): Observable<Task> {
        return this.http.post(`${this.API_URL}/create`, task).pipe(
            map((res: any) => {
                const response = ApiResponse.fromJson<Task>(res);
                if (!response.isSuccessful) {
                    throw new Error(response.message);
                }

                return response.data;
            }),
            catchError(error => {
                return throwError(() => new Error(error.message ?? error));
            })
        );
    }

    updateTask(id: number, task: CreateTaskRequest): Observable<Task> {
        return this.http.patch(`${this.API_URL}/update/${id}`, task).pipe(
            map((res: any) => {
                const response = ApiResponse.fromJson<Task>(res);
                if (!response.isSuccessful) {
                    throw new Error(response.message);
                }

                return response.data;
            }),
            catchError(error => {
                return throwError(() => new Error(error.message ?? error));
            })
        );
    }

    deleteTask(id: number): Observable<Task[]> {
        return this.http.delete(`${this.API_URL}/delete/${id}`).pipe(
            map((res: any) => {
                const response = ApiResponse.fromJson<Task[]>(res);
                if (!response.isSuccessful) {
                    throw new Error(response.message);
                }

                return response.data;
            }),
            catchError(error => {
                return throwError(() => new Error(`Error deleting task: ${error.message}`));
            })
        );
    }

    searchTasks(query: string, category?: string): Observable<Task[]> {
        let url = `${this.API_URL}/search?query=${query}`;
        if (category) {
            url += `&category=${category}`;
        }

        return this.http.get<ApiResponse<Task[]>>(url).pipe(
            map((res: any) => {
                const response = ApiResponse.fromJson<Task[]>(res);
                if (!response.isSuccessful) {
                    throw new Error(response.message);
                }

                return response.data;
            }),
            catchError(error => {
                return throwError(() => new Error(`Error searching tasks: ${error.message}`));
            })
        );
    }
}