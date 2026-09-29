import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageUsers } from './manage-users';

describe('ManageUsers', () => {
  let component: ManageUsers;
  let fixture: ComponentFixture<ManageUsers>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ManageUsers]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageUsers);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
