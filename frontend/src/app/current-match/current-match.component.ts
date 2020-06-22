import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {select, Store} from '@ngrx/store';
import {ScoreConfirmationModalComponent} from '../score-confirmation-modal/score-confirmation-modal.component';
import {SubscriptionHelper} from '../utils/subscription-helper';
import {MatchEO, MatchWithPlayers} from '../entities/match';
import {PlayerEO} from '../entities/playersl';
import {combineLatest} from 'rxjs';
import {selectCurrentMatch, selectPlayersList} from '../store/players/players.selectors';
import {GlobalStore} from '../store/global';

@Component({
  selector: 'app-current-match',
  templateUrl: './current-match.component.html',
  styleUrls: ['./current-match.component.css']
})
export class CurrentMatchComponent extends SubscriptionHelper implements OnInit, OnDestroy {

  public currentMatch: MatchWithPlayers;

  public blueScoreList: number[] = [8, 7, 6, 5, 4, 3, 2, 1, 0];
  public redScoreList: number[] = [0, 1, 2, 3, 4, 5, 6, 7, 8];
  public blueScore = -1;
  public redScore = -1;

  @ViewChild('confirm')
  private confirmDialog: ScoreConfirmationModalComponent;

  constructor(private store: Store<GlobalStore>) {
    super();
  }

  ngOnInit() {
    this.addSubscription(
      combineLatest([
        this.store.pipe(select('matches'), select('currentMatch')),
        this.store.pipe(select('players'), select('list'))
      ])
        .subscribe((arr: [MatchEO, PlayerEO[]]) =>
          this.currentMatch = MatchWithPlayers.createMatchWithPlayer(arr[0], arr[1])
        ));
  }

  ngOnDestroy(): void {
    this.unsubscribeAll();
  }

  public saveScore(): void {
    if (this.currentMatch) {
      const match = Object.assign({}, this.currentMatch.match);
      match.blueScore = this.blueScore;
      match.redScore = this.redScore;
      this.currentMatch.match = match;
      this.confirmDialog.confirmMatchResult(this.currentMatch);
    }
  }

  public autoFill(): void {
    if (this.blueScore === -1 && this.redScore !== 7) {
      this.blueScore = 7;
    }
    if (this.redScore === -1 && this.blueScore !== 7) {
      this.redScore = 7;
    }
  }
}
