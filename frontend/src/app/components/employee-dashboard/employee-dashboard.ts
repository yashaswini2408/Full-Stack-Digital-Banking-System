import { Component, Renderer2, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-employee-dashboard',
  standalone: false,
  templateUrl: './employee-dashboard.html',
  styleUrls: ['./employee-dashboard.css']
})
export class EmployeeDashboard implements OnInit {
  isCollapsed = false;
  userId: number = 0;

  constructor(
    public renderer: Renderer2,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    // ✅ Get userId dynamically from route if needed
    this.route.paramMap.subscribe(params => {
      const id = params.get('userId');
      if (id) {
        this.userId = +id;
        console.log('Employee Dashboard Loaded for User ID:', this.userId);
      }
    });

    // Sidebar toggle logic (optional UI feature)
    const toggleBtn = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('sidebar');

    toggleBtn?.addEventListener('click', () => {
      this.isCollapsed = !this.isCollapsed;
      sidebar?.classList.toggle('collapsed', this.isCollapsed);
    });
  }

  logout() {
    this.router.navigate(['/login']); // Just redirect without clearing token/session
  }
}
