import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PlayersSelectionComponent } from './players-selection.component';

describe('PlayersSelectionComponent', () => {
  let component: PlayersSelectionComponent;
  let fixture: ComponentFixture<PlayersSelectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PlayersSelectionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PlayersSelectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
