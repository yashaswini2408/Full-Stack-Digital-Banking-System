import { TestBed } from '@angular/core/testing';

import { BankBranchService } from './bank-branch.service';

describe('BankBranchService', () => {
  let service: BankBranchService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BankBranchService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
