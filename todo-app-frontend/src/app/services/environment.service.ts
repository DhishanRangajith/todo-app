import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class EnvironmentService {

  constructor() { }

  get apiUrl(): string {
    return (window as any).__env?.apiUrl || '';
  }
  
  // get environment(): string {
  //   return (window as any).__env?.environment || '';
  // }
}
