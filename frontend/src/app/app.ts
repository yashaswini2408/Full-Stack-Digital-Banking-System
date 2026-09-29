import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  standalone: false,
  styleUrl: './app.css'
})
export class App implements OnInit {
  protected title = 'frontend';
  showNav = true;

  constructor(private router: Router) {
    // Toggle navigation bar visibility based on the current route
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.showNav = !(event.url.includes('/login') || event.url.includes('/register'));
      }
    });
  }

  ngOnInit(): void {
    // You can choose to remove this logic or navigate to home
    // Removed: JWT token/session restoration
  }
}
