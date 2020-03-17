import {Component, OnDestroy, OnInit} from '@angular/core';
import {SubscriptionHelper} from '../../utils/subscription-helper';
import {PlayerEO} from '../../entities/playersl';
import {select, Store} from '@ngrx/store';
import {MatchEO, MatchWithPlayers} from '../../entities/match';
import {combineLatest} from 'rxjs';
import {selectLastMatches, selectPlayersList} from '../../store/players/players.selectors';
import {GlobalStore} from '../../store/global';

export class MatchWithWinnerLooser extends MatchWithPlayers {

  private blueWon: boolean;

  constructor(matchWithPlayers: MatchWithPlayers) {
    super(matchWithPlayers.match);
    Object.assign(this, matchWithPlayers);
    this.blueWon = this.match.blueScore > this.match.redScore;
  }


  public get winners(): PlayerEO[] {
    if (this.blueWon) {
      return [this.playerBlueDefense, this.playerBlueOffense];
    } else {
      return [this.playerRedDefense, this.playerRedOffense];
    }
  }

  public get loosers(): PlayerEO[] {
    if (this.blueWon) {
      return [this.playerRedDefense, this.playerRedOffense];
    } else {
      return [this.playerBlueDefense, this.playerBlueOffense];
    }
  }

  public get score(): string {
    if (this.blueWon) {
      return `${this.match.blueScore}-${this.match.redScore}`;
    } else {
      return `${this.match.redScore}-${this.match.blueScore}`;
    }
  }
}

@Component({
  selector: 'app-last-matches',
  templateUrl: './last-matches.component.html',
  styleUrls: ['./last-matches.component.css']
})
export class LastMatchesComponent extends SubscriptionHelper implements OnInit, OnDestroy {

  public matches: MatchWithWinnerLooser[];

  constructor(private store: Store<GlobalStore>) {
    super();
  }

  ngOnInit() {
    this.addSubscription(
      combineLatest([
        this.store.pipe(select('matches'), select('lastMatches')),
        this.store.pipe(select('players'), select('list'))
      ])
        .subscribe((arr: [MatchEO[], PlayerEO[]]) => {
          this.matches = arr[0].map(m => new MatchWithWinnerLooser(MatchWithPlayers.createMatchWithPlayer(m, arr[1])));
        }));
  }

  ngOnDestroy(): void {
    this.unsubscribeAll();
  }

}
