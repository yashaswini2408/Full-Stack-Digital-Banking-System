import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplyLoan } from './apply-loan';

describe('ApplyLoan', () => {
  let component: ApplyLoan;
  let fixture: ComponentFixture<ApplyLoan>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ApplyLoan]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ApplyLoan);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
