import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerRegister } from './customer-register';

describe('CustomerRegister', () => {
  let component: CustomerRegister;
  let fixture: ComponentFixture<CustomerRegister>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomerRegister]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CustomerRegister);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
