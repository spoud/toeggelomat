import {Component, OnDestroy, OnInit} from '@angular/core';
import {SubscriptionHelper} from '../../utils/subscription-helper';
import {PlayerEO} from '../../entities/players';
import {select, Store} from '@ngrx/store';
import {GlobalStore} from '../../store/global';
import {SpoudAvatarComponent} from "../../spoud-avatar/spoud-avatar.component";
import {LastMatchTimePipe} from "./last-match-time.pipe";
import {CommonModule} from "@angular/common";

@Component({
  standalone: true,
  selector: 'app-players-scoreboard',
  templateUrl: './players-scoreboard.component.html',
  styleUrls: ['./players-scoreboard.component.css'],
  imports: [
    CommonModule,
    SpoudAvatarComponent,
    LastMatchTimePipe
  ]
})
export class PlayersScoreboardComponent extends SubscriptionHelper implements OnInit, OnDestroy {

  public players: PlayerEO[] = [];

  constructor(private store: Store<GlobalStore>) {
    super();
  }

  ngOnInit() {
    this.addSubscription(this.store.pipe(select('players'), select('list'))
      .subscribe((list: PlayerEO[]) => {
        this.players = list.slice()
          .filter(player => player.lastMatchTime)
          .sort((l, r) => r.offensePoints + r.defensePoints - l.offensePoints - l.defensePoints);
      }));
  }

  ngOnDestroy(): void {
    this.unsubscribeAll();
  }
}
