import {Component, OnInit, ViewChild} from '@angular/core';
import {select, Store} from '@ngrx/store';
import {MatchWithPlayers} from '../store/matches/matches.reducer';
import {ScoreConfirmationModalComponent} from '../score-confirmation-modal/score-confirmation-modal.component';

@Component({
  selector: 'app-current-match',
  templateUrl: './current-match.component.html',
  styleUrls: ['./current-match.component.css']
})
export class CurrentMatchComponent implements OnInit {

  public currentMatch: MatchWithPlayers;

  public blueScoreList: number[] = [9, 8, 7, 6, 5, 4, 3, 2, 1, 0];
  public redScoreList: number[] = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9];
  public blueScore = -1;
  public redScore = -1;
  public test = 'hello';

  @ViewChild('confirm', {static: false})
  private confirmDialog: ScoreConfirmationModalComponent;

  constructor(private store: Store<{ count: number }>) {
  }

  ngOnInit() {
    // FIXME unsubscribe
    this.store.pipe(select('matches'), select('currentMatch'))
      .subscribe(match => this.currentMatch = match);
  }

  public saveScore(): void {
    if (this.currentMatch) {
      const match = this.currentMatch.match;
      match.blueScore = this.blueScore;
      match.redScore = this.redScore;
      this.confirmDialog.confirmMatchResult(this.currentMatch);
    }
  }
}
