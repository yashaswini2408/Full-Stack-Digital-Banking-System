import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageBankEmployees } from './manage-bank-employees';

describe('ManageBankEmployees', () => {
  let component: ManageBankEmployees;
  let fixture: ComponentFixture<ManageBankEmployees>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ManageBankEmployees]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageBankEmployees);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
