import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../models/user';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://localhost:3000/users'; // json-server url
  private userApiUrl = 'http://localhost:8080/api/v1/users';
  private adminApiUrl = 'http://localhost:8080/admin';

  constructor(private http: HttpClient) {}

  registerUser(user: User): Observable<any> {
  return this.http.post('http://localhost:8080/api/v1/users/register', user, {
    headers: { 'Content-Type': 'application/json' }
  });
  }
  // Get all users, map id to userId
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.adminApiUrl}/getallusers`);
  }
  // Get one user by userId
  getUserById(userId: number): Observable<User> {
    return this.http.get<User>(`${this.userApiUrl}/getUserById/${userId}`);
  }

  // Add user
  addUser(user: User): Observable<User> {
    return this.registerUser(user);
  }

  // Update user by userId
  updateUser(userId: number, user: User): Observable<User> {
    return this.http.put<User>(`${this.userApiUrl}/updateUser/${userId}`, user);
  }

  // Delete user by userId
   deleteUser(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.userApiUrl}/deleteUser/${userId}`);
  }

}
