import {Component, OnInit, ViewChild} from '@angular/core';
import {NgbModal, NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import {saveMatchScore} from '../store/matches/maches.actions';
import {Store} from '@ngrx/store';
import {interval, Subscription} from 'rxjs';
import {take} from 'rxjs/operators';
import {MatchWithPlayers} from '../entities/match';


@Component({
    selector: 'app-score-confirmation-modal',
    templateUrl: './score-confirmation-modal.component.html',
    styleUrls: ['./score-confirmation-modal.component.css'],
    imports: [
    NgbModalModule
]
})
export class ScoreConfirmationModalComponent implements OnInit {

  @ViewChild('content')
  private content: any;

  public redWon?: boolean;
  public match?: MatchWithPlayers;
  public timeout: number = 0;
  private intervalSubscription?: Subscription;

  constructor(private modalService: NgbModal, private store: Store<{ count: number }>) {
  }

  ngOnInit() {
  }

  public confirmMatchResult(match: MatchWithPlayers): void {
    this.match = match;
    this.redWon = match.match.redScore > match.match.blueScore;

    const modalRef = this.modalService.open(this.content, {size: 'lg'}).result.then((result) => {
      if (result === 'save') {
        this.confirmScore();
      }
      this.intervalSubscription?.unsubscribe();
    });

    this.timeout = 10;
    this.intervalSubscription = interval(1000).pipe(
      take(10)
    ).subscribe(() => {
      this.timeout--;
      if (this.timeout === 0) {
        this.confirmScore();
        this.modalService.dismissAll();
      }
    });
  }

  private confirmScore(): void {
    console.log('confirm match', this.match);
    if (this.match) {
      this.store.dispatch(saveMatchScore({match: this.match.match}));
    }
  }

}
