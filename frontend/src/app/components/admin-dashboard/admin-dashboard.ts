import { Component, OnInit, Renderer2 } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  standalone: false,
  templateUrl: './admin-dashboard.html',
  styleUrls: ['./admin-dashboard.css']
})
export class AdminDashboard implements OnInit {

  isCollapsed = false;
  username = '';
  roleName = '';

  constructor(
    public renderer: Renderer2,
    private router: Router
  ) {}

  ngOnInit(): void {
  const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
  if (!user || !user.roleName || user.roleName.toLowerCase() !== 'administrator') {
    this.router.navigate(['/login']);
    return;
  }
  this.username = user.username ?? '';
  this.roleName = user.roleName ?? 'Administrator';
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}