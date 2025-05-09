import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';

import { TodoCreateComponent } from './todo-create.component';
import { TaskService } from '../../services/task.service';
import { ToastrService } from 'ngx-toastr';

describe('TodoCreateComponent', () => {
  let component: TodoCreateComponent;
  let fixture: ComponentFixture<TodoCreateComponent>;
  let taskServiceSpy: jasmine.SpyObj<TaskService>;
  let toastrServiceSpy: jasmine.SpyObj<ToastrService>;

  beforeEach(async () => {
    const taskSpy = jasmine.createSpyObj('TaskService', ['createTask', 'refreshTaskList']);
    const toastrSpy = jasmine.createSpyObj('ToastrService', ['success', 'error']);

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      declarations: [TodoCreateComponent],
      providers: [
        { provide: TaskService, useValue: taskSpy },
        { provide: ToastrService, useValue: toastrSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TodoCreateComponent);
    component = fixture.componentInstance;
    taskServiceSpy = TestBed.inject(TaskService) as jasmine.SpyObj<TaskService>;
    toastrServiceSpy = TestBed.inject(ToastrService) as jasmine.SpyObj<ToastrService>;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty fields', () => {
    expect(component.form).toBeDefined();
    expect(component.form.value).toEqual({ title: '', description: '' });
  });

  it('should not submit if form is invalid', () => {
    component.form.controls['title'].setValue('');
    component.onSubmit();
    expect(taskServiceSpy.createTask).not.toHaveBeenCalled();
  });

  it('should call createTask and reset form on success', fakeAsync(() => {
    component.form.setValue({ title: 'Test Task', description: 'Test Desc' });
    // taskServiceSpy.createTask.and.returnValue(of({}));

    component.onSubmit();
    tick(); // simulate async

    expect(taskServiceSpy.createTask).toHaveBeenCalled();
    expect(taskServiceSpy.refreshTaskList).toHaveBeenCalled();
    expect(toastrServiceSpy.success).toHaveBeenCalledWith('Task created!', 'Success');
    expect(component.form.value).toEqual({ title: null, description: null });
  }));

  it('should show error toast on failure', fakeAsync(() => {
    component.form.setValue({ title: 'Error Task', description: 'Error Desc' });
    const errorResponse = { error: { description: 'Create failed' } };
    taskServiceSpy.createTask.and.returnValue(throwError(() => errorResponse));

    component.onSubmit();
    tick();

    expect(toastrServiceSpy.error).toHaveBeenCalledWith('Create failed', 'Task created fail');
  }));
});
