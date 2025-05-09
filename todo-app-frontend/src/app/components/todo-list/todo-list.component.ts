import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { LoadingComponent } from '../../shared/loading/loading.component';
import { ToDoTask } from '../../models/todotask.model';
import { TodoViewComponent } from '../todo-view/todo-view.component';
import { Subscription } from 'rxjs';
import { TaskService } from '../../services/task.service';
import { ToastrService } from 'ngx-toastr';
import { APP_CONSTANTS } from '../../constants/app-constants';

@Component({
  selector: 'app-todo-list',
  imports: [
    CommonModule,
    LoadingComponent,
    TodoViewComponent
  ],
  templateUrl: './todo-list.component.html',
  styleUrl: './todo-list.component.scss'
})
export class TodoListComponent implements OnInit, OnDestroy{

  isLoading !: boolean;
  toDoTaskList !: ToDoTask[];
  private fetchTaskListsubscription !: Subscription;

  constructor(
    private taskService : TaskService,
    private toastr: ToastrService
  ){}

  ngOnInit(): void {
    this.toDoTaskList = [];
    this.fetchTaskListsubscription = this.taskService.callFetchTaskList$.subscribe(() => {
      this.fetchToDoTaskList(); 
    });

    this.fetchToDoTaskList();
  }

  fetchToDoTaskList(){
    this.isLoading = true;
    this.taskService.getTasks().subscribe(
      {
        next: (response: any) => {
          this.toDoTaskList = response.content;
          this.isLoading = false;
        },
        error: (error) => {
          this.toastr.error(error.error.description, 'Task created fail');
          this.isLoading = false;
          
        }
      }
    );
  }

  ngOnDestroy(): void {
    if (this.fetchTaskListsubscription) {
      this.fetchTaskListsubscription.unsubscribe(); 
    }
  }

  handleTaskDone(taskId: number){
    this.isLoading = true;
    this.taskService.updateTaskStatus(taskId, APP_CONSTANTS.STATUS_INACTIVE).subscribe(
      {
        next: (response: any) => {
          this.fetchToDoTaskList();
          this.toastr.success('Task is Done!', 'Success');
          this.isLoading = false;
        },
        error: (error) => {
          this.toastr.error(error.error.description, 'Task done fail');
          this.isLoading = false;
        }
      }
    );
  }
  
}
