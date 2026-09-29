import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenAccount } from './open-account';

describe('OpenAccount', () => {
  let component: OpenAccount;
  let fixture: ComponentFixture<OpenAccount>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OpenAccount]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OpenAccount);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
