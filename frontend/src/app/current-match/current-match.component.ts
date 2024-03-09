import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {select, Store} from '@ngrx/store';
import {ScoreConfirmationModalComponent} from '../score-confirmation-modal/score-confirmation-modal.component';
import {SubscriptionHelper} from '../utils/subscription-helper';
import {MatchEO, MatchWithPlayers} from '../entities/match';
import {PlayerEO} from '../entities/players';
import {combineLatest} from 'rxjs';
import {GlobalStore} from '../store/global';
import {CommonModule} from "@angular/common";

@Component({
  standalone: true,
  selector: 'app-current-match',
  templateUrl: './current-match.component.html',
  styleUrls: ['./current-match.component.css'],
  imports: [
    CommonModule,
    ScoreConfirmationModalComponent
  ]
})
export class CurrentMatchComponent extends SubscriptionHelper implements OnInit, OnDestroy {

  public currentMatch?: MatchWithPlayers;

  public blueScoreList: number[] = [8, 7, 6, 5, 4, 3, 2, 1, 0];
  public redScoreList: number[] = [0, 1, 2, 3, 4, 5, 6, 7, 8];
  public blueScore = -1;
  public redScore = -1;

  @ViewChild('confirm')
  private confirmDialog?: ScoreConfirmationModalComponent;

  constructor(private store: Store<GlobalStore>) {
    super();
  }

  ngOnInit() {
    this.addSubscription(
      combineLatest([
        this.store.pipe(select('matches'), select('currentMatch')),
        this.store.pipe(select('players'), select('list'))
      ])
        .subscribe({
            next: ([match, players]: [MatchEO | undefined, PlayerEO[]]) => {
              if (match) {
                this.currentMatch = MatchWithPlayers.createMatchWithPlayer(match, players)
              }
            }
          }
        )
    );
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
      this.confirmDialog?.confirmMatchResult(this.currentMatch);
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
