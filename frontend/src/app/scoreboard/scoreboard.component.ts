import {Component, OnDestroy, OnInit} from '@angular/core';
import { select, Store } from '@ngrx/store';
import { PlayerEO } from '../entities/playersl';
import {SubscriptionHelper} from '../utils/subscription-helper';

@Component({
  selector: 'app-scoreboard',
  templateUrl: './scoreboard.component.html',
  styleUrls: ['./scoreboard.component.css']
})
export class ScoreboardComponent extends SubscriptionHelper implements OnInit, OnDestroy {

  public players: PlayerEO[];

  constructor(private store: Store<{ count: number }>) {
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
