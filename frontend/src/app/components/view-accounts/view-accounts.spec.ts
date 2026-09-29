import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewAccounts } from './view-accounts';

describe('ViewAccounts', () => {
  let component: ViewAccounts;
  let fixture: ComponentFixture<ViewAccounts>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewAccounts]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewAccounts);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
