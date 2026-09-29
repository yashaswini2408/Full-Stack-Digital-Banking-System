import { Component, OnInit } from '@angular/core';
import { User } from '../../models/user';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-manage-users',
  standalone: false,
  templateUrl: './manage-users.html',
  styleUrl: './manage-users.css'
})
export class ManageUsers implements OnInit {
  users: User[] = [];
  filteredUsers: User[] = [];
  selectedUser?: User;
  isEditMode = false;
  searchUserId: string = '';

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getUsers().subscribe(data => {
      this.users = data;
      this.filteredUsers = data;
    });
  }

  searchUser(): void {
    const id = this.searchUserId.trim();
if (!id) {
  this.filteredUsers = this.users;
} else {
  const numericId = +id;
  this.filteredUsers = this.users.filter(user =>
    user.userId === numericId
  );
}

  }

  selectUser(user: User): void {
    this.selectedUser = { ...user }; // copy to avoid direct mutation
    this.isEditMode = true;
  }

  clearSelection(): void {
    this.selectedUser = undefined;
    this.isEditMode = false;
  }

  saveUser(): void {
    if (!this.selectedUser) return;

    if (this.isEditMode && this.selectedUser.userId) {
      this.userService.updateUser(this.selectedUser.userId, this.selectedUser).subscribe(() => {
        this.loadUsers();
        this.clearSelection();
      });
    } else {
      this.userService.addUser(this.selectedUser).subscribe(() => {
        this.loadUsers();
        this.clearSelection();
      });
    }
  }

  deleteUser(userId?: number): void {
  if (userId == null) return;
  this.userService.deleteUser(userId).subscribe(() => {
    this.loadUsers();
  });
}

  

  newUser(): void {
    this.selectedUser = { username: '', email: '', password: '', roleId: 1 };
    this.isEditMode = false;
  }
}