import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransactionView } from './transaction-view';

describe('TransactionView', () => {
  let component: TransactionView;
  let fixture: ComponentFixture<TransactionView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TransactionView]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransactionView);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
