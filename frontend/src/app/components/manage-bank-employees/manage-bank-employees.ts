import { Component, OnInit } from '@angular/core';
import { BankEmployee } from '../../models/bank-employee';
import { BankEmployeeService } from '../../services/bank-employee.service';

@Component({
  selector: 'app-manage-bank-employees',
  standalone: false,
  templateUrl: './manage-bank-employees.html',
  styleUrl: './manage-bank-employees.css'
})
export class ManageBankEmployees implements OnInit {
  employees: BankEmployee[] = [];
  filteredEmployees: BankEmployee[] = [];
  selectedEmployee?: BankEmployee;
  isEditMode = false;
  searchId: string = '';

  constructor(private service: BankEmployeeService) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.service.getAllBankEmployees().subscribe(data => {
      this.employees = data;
      this.filteredEmployees = [...data];
    });
  }

  searchEmployee(): void {
    const id = this.searchId.trim();
    if (!id) {
      this.filteredEmployees = this.employees;
    } else {
      const numericId = Number(id);
      this.filteredEmployees = this.employees.filter(emp => emp.userId === numericId);
    }
  }

  selectEmployee(emp: BankEmployee): void {
    this.selectedEmployee = { ...emp };
    this.isEditMode = true;
  }

  clearSelection(): void {
    this.selectedEmployee = undefined;
    this.isEditMode = false;
  }

  saveEmployee(): void {
    if (!this.selectedEmployee) return;

    const employee = {
      ...this.selectedEmployee,
      userId: Number(this.selectedEmployee.userId),
      branchId: Number(this.selectedEmployee.branchId)
    };

    if (this.isEditMode) {
      this.service.updateBankEmployee(employee.userId, employee).subscribe({
        next: () => {
          this.loadEmployees();
          this.clearSelection();
        },
        error: err => console.error('Update failed:', err)
      });
    } else {
      this.service.createBankEmployee(employee).subscribe({
        next: () => {
          this.loadEmployees();
          this.clearSelection();
        },
        error: err => console.error('Create failed:', err)
      });
    }
  }

  deleteEmployee(userId: number): void {
    this.service.deleteBankEmployee(Number(userId)).subscribe({
      next: () => this.loadEmployees(),
      error: err => console.error('Delete failed:', err)
    });
  }

  newEmployee(): void {
    this.selectedEmployee = {
      userId: 0,
      name: '',
      contactNumber: '',
      branchId: 0
    };
    this.isEditMode = false;
  }
}