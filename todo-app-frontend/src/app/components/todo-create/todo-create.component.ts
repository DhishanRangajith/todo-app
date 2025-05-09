import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { LoadingComponent } from '../../shared/loading/loading.component';
import { TaskService } from '../../services/task.service';
import { ToDoTask } from '../../models/todotask.model';
import { ToastrService } from 'ngx-toastr';
import { APP_CONSTANTS } from '../../constants/app-constants';

@Component({
  selector: 'app-todo-create',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    LoadingComponent
  ],
  templateUrl: './todo-create.component.html',
  styleUrl: './todo-create.component.scss'
})
export class TodoCreateComponent implements OnInit {

  form!: FormGroup;
  isLoading!: boolean;

  constructor(
    private fb: FormBuilder,
    private taskService: TaskService,
    private toastr: ToastrService
  ){

  }

  ngOnInit(): void {
    this.isLoading = false;
    this.form = this.fb.group(
      {
        title: ['', Validators.required],
        description: ['']
      }
    );
  }

  onSubmit(): void {
    this.form.markAllAsTouched();
    if(this.form.valid){
      const data: ToDoTask = this.form.value;
      data.status = APP_CONSTANTS.STATUS_ACTIVE;
      this.isLoading = true;
      this.taskService.createTask(data).subscribe(
        {
          next: (response) => {
            this.form.reset();
            this.taskService.refreshTaskList();
            this.toastr.success('Task created!', 'Success');
          },
          error: (error) => {
            this.toastr.error(error.error.description, 'Task created fail');
          },
          complete: () => {
            this.isLoading = false;
          }
        }
      );
    }
  }

}
