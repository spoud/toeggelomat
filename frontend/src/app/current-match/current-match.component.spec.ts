import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrentMatchComponent } from './current-match.component';

describe('CurrentMatchComponent', () => {
  let component: CurrentMatchComponent;
  let fixture: ComponentFixture<CurrentMatchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CurrentMatchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CurrentMatchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
