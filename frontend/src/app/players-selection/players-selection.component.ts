import {Component, computed, inject} from '@angular/core';
import {SpoudAvatarComponent} from "../spoud-avatar/spoud-avatar.component";

import {Router, RouterModule} from "@angular/router";
import {PlayersService} from "../services/players-service";
import {Player} from "../../generated/graphql";
import {MatchesService} from "../services/matches-service";

export class SelectablePlayer {
  constructor(public player: Player, public selected: boolean = false) {
  }
}

@Component({
  selector: 'app-players-selection',
  templateUrl: './players-selection.component.html',
  styleUrls: ['./players-selection.component.css'],
  imports: [
    RouterModule,
    SpoudAvatarComponent
  ]
})
export class PlayersSelectionComponent {

  private playersService = inject(PlayersService);
  private matchService = inject(MatchesService);
  private router = inject(Router);

  public players = computed(() => {
    return this.playersService.players()
      .slice()
      .sort((l, r) => l.nickName.localeCompare(r.nickName)) // TODO sort by last match time?
      .map(p => new SelectablePlayer(p));
  })

  public enoughToStart = false;
  public playerCount = 0;

  private updateConditions(): void {
    this.playerCount = this.players().filter(p => p.selected).length;
    this.enoughToStart = this.playerCount >= 4;
  }

  public selectPlayer(player: SelectablePlayer) {
    player.selected = !player.selected;
    this.updateConditions();
  }

  public startMatch(): void {
    this.matchService.startMatch(this.players()
      .filter(p => p.selected)
      .map(p => p.player.uuid));
    this.router.navigate(['current-match']);
  }
}
