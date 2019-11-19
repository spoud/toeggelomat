import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LastMatchesComponent } from './last-matches.component';

describe('LastMatchesComponent', () => {
  let component: LastMatchesComponent;
  let fixture: ComponentFixture<LastMatchesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LastMatchesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LastMatchesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
