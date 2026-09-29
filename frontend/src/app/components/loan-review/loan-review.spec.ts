import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoanReview } from './loan-review';

describe('LoanReview', () => {
  let component: LoanReview;
  let fixture: ComponentFixture<LoanReview>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoanReview]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoanReview);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
