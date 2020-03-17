import {Component, OnDestroy, OnInit} from '@angular/core';
import {SubscriptionHelper} from '../../utils/subscription-helper';
import {PlayerEO} from '../../entities/playersl';
import {select, Store} from '@ngrx/store';
import {selectPlayersList} from '../../store/players/players.selectors';
import {GlobalStore} from '../../store/global';

@Component({
  selector: 'app-players-scoreboard',
  templateUrl: './players-scoreboard.component.html',
  styleUrls: ['./players-scoreboard.component.css']
})
export class PlayersScoreboardComponent extends SubscriptionHelper implements OnInit, OnDestroy {

  public players: PlayerEO[];

  constructor(private store: Store<GlobalStore>) {
    super();
  }

  ngOnInit() {
    this.addSubscription(this.store.pipe(select('players'), select('list'))
      .subscribe((list: PlayerEO[]) => {
        this.players = list.sort((l, r) => r.offensePoints + r.defensePoints - l.offensePoints - l.defensePoints);
      }));
  }

  ngOnDestroy(): void {
    this.unsubscribeAll();
  }
}
