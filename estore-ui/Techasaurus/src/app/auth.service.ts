import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private userIsAdmin = false;

  constructor() { }

  login() {
    // Your login logic here
    // For example, set the userIsAdmin flag to true if the user is an admin
    this.userIsAdmin = true;
  }

  logout() {
    // Your logout logic here
    // For example, set the userIsAdmin flag to false
    this.userIsAdmin = false;
  }

  isAdmin() {
    // Return true if the user is an admin, false otherwise
    return this.userIsAdmin;
  }
}
