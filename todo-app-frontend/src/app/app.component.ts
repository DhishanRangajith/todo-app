import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TodoCreateComponent } from './components/todo-create/todo-create.component';
import { TodoListComponent } from './components/todo-list/todo-list.component';

@Component({
  selector: 'app-root',
  imports: [
    // RouterOutlet,
    TodoCreateComponent,
    TodoListComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'todo-app-frontend';
}
