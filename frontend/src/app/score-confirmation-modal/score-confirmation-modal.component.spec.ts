import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScoreConfirmationModalComponent } from './score-confirmation-modal.component';

describe('ScoreConfirmationModalComponent', () => {
  let component: ScoreConfirmationModalComponent;
  let fixture: ComponentFixture<ScoreConfirmationModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScoreConfirmationModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScoreConfirmationModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
