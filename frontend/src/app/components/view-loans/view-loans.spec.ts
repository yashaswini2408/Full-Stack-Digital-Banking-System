import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewLoans } from './view-loans';

describe('ViewLoans', () => {
  let component: ViewLoans;
  let fixture: ComponentFixture<ViewLoans>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewLoans]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewLoans);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
