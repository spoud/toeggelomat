import {Component, OnDestroy, OnInit} from '@angular/core';
import {select, Store} from '@ngrx/store';
import {PlayerEO} from '../entities/playersl';
import {startMatch} from '../store/matches/maches.actions';
import {SubscriptionHelper} from '../utils/subscription-helper';

export class SelectablePlayer {
  constructor(public player: PlayerEO, public selected: boolean = false) {
  }
}

@Component({
  selector: 'app-players-selection',
  templateUrl: './players-selection.component.html',
  styleUrls: ['./players-selection.component.css']
})
export class PlayersSelectionComponent extends SubscriptionHelper implements OnInit, OnDestroy {

  public players: SelectablePlayer[];
  public enoughToStart = false;
  public playerCount = 0;

  constructor(private store: Store<{ count: number }>) {
    super();
  }

  ngOnInit() {
    this.addSubscription(this.store.pipe(select('players'), select('list'))
      .subscribe((list: PlayerEO[]) => {
        this.players = list
          .sort((l, r) => l.nickName.localeCompare(r.nickName))
          .map(p => new SelectablePlayer(p));
        this.updateConditions();
      }));
  }

  ngOnDestroy(): void {
    this.unsubscribeAll();
  }

  private updateConditions(): void {
    this.playerCount = this.players.filter(p => p.selected).length;
    this.enoughToStart = this.playerCount >= 4;
  }

  public selectPlayer(player: SelectablePlayer) {
    player.selected = !player.selected;
    this.updateConditions();
  }

  public startMatch(): void {
    this.store.dispatch(startMatch({
      playerUuids: this.players
        .filter(p => p.selected)
        .map(p => p.player.uuid)
    }));
  }
}
