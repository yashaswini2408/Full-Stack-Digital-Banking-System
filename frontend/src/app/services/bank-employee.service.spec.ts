import { TestBed } from '@angular/core/testing';

import { BankEmployeeService } from './bank-employee.service';

describe('BankEmployeeService', () => {
  let service: BankEmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BankEmployeeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
