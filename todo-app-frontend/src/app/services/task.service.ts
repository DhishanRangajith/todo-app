import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { ToDoTask } from '../models/todotask.model';
import { EnvironmentService } from './environment.service';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private apiUrl!: string;
  private methodCalledSource = new Subject<void>();
  callFetchTaskList$ = this.methodCalledSource.asObservable();

  constructor(private environmentService: EnvironmentService, private http: HttpClient) {
    this.apiUrl = `${this.environmentService.apiUrl}/tasks`;
    console.log('this.apiUrl',this.apiUrl);
  }

  refreshTaskList(){
    this.methodCalledSource.next();
  }

  getTasks(): Observable<ToDoTask[]> {
    return this.http.get<ToDoTask[]>(this.apiUrl);
  }

  createTask(task: ToDoTask): Observable<ToDoTask> {
    return this.http.post<ToDoTask>(this.apiUrl, task);
  }

  updateTaskStatus(taskId: number, status: number): Observable<ToDoTask> {
    const data = {
      status: status
    };
    return this.http.patch<ToDoTask>(`${this.apiUrl}/${taskId}/status`, data);
  }
  
}
