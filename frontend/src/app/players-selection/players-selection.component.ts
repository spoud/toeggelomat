import {Component, OnInit} from '@angular/core';
import {select, Store} from '@ngrx/store';
import {PlayerEO} from '../entities/playersl';
import {startMatch} from '../store/matches/maches.actions';

export class SelectablePlayer {
  constructor(public player: PlayerEO, public selected: boolean = true) { // TODO false
  }
}

@Component({
  selector: 'app-players-selection',
  templateUrl: './players-selection.component.html',
  styleUrls: ['./players-selection.component.css']
})
export class PlayersSelectionComponent implements OnInit {

  public players: SelectablePlayer[];
  public enoughToStart = false;
  public playerCount = 0;

  constructor(private store: Store<{ count: number }>) {
    // FIXME unsubscribe
    store.pipe(select('players'), select('list'))
      .subscribe(list => {
        this.players = list.map(p => new SelectablePlayer(p));
        this.updateConditions();
      });
  }

  ngOnInit() {
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
