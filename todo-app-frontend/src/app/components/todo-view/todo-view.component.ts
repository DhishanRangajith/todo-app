import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ToDoTask } from '../../models/todotask.model';

@Component({
  selector: 'app-todo-view',
  imports: [],
  templateUrl: './todo-view.component.html',
  styleUrl: './todo-view.component.scss'
})
export class TodoViewComponent {

  @Input()
  toDoTask !: ToDoTask

  @Output() 
  doneTask = new EventEmitter<number>();

  onDone(){
    this.doneTask.emit(this.toDoTask.id);
  }

}
