import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpoudAvatarComponent } from './spoud-avatar.component';

describe('SpoudAvatarComponent', () => {
  let component: SpoudAvatarComponent;
  let fixture: ComponentFixture<SpoudAvatarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpoudAvatarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpoudAvatarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
