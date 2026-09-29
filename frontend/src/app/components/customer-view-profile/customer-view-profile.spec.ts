import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerViewProfile } from './customer-view-profile';

describe('CustomerViewProfile', () => {
  let component: CustomerViewProfile;
  let fixture: ComponentFixture<CustomerViewProfile>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomerViewProfile]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CustomerViewProfile);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
