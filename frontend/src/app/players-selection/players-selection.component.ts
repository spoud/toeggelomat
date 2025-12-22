import {Component, computed, inject, signal} from '@angular/core';
import {SpoudAvatarComponent} from "../spoud-avatar/spoud-avatar.component";

import {RouterModule} from "@angular/router";
import {PlayersService} from "../services/players-service";
import {Player} from "../../generated/graphql";
import {MatchesService} from "../services/matches-service";

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

  sortByName = signal<boolean>(false);
  players = computed(() => {
    let sortByName = this.sortByName();
    return this.playersService.players()
      .slice()
      .sort((l, r) => this.comparePlayer(sortByName, l, r));
  })
  selectedPlayers = signal(new Set<string>());
  playerCount = computed(() => this.selectedPlayers().size);
  enoughToStart = computed(() => this.playerCount() >= 4);

  public selectPlayer(player: Player) {
    this.selectedPlayers.update(list => {
      const set = new Set(list);
      if (set.has(player.uuid)) {
        set.delete(player.uuid);
      } else {
        set.add(player.uuid);
      }
      return set;
    });
  }

  public startMatch(): void {
    this.matchService.startMatch([...this.selectedPlayers().values()]);
  }

  private comparePlayer(sortByName: boolean, left: Player, right: Player): number {
    if (sortByName) {
      return this.comparePlayerByNickName(left, right);
    } else {
      return this.comparePlayerByLastMatch(left, right)
    }
  }

  private comparePlayerByNickName(left: Player, right: Player): number {
    return left.nickName.localeCompare(right.nickName);
  }

  private comparePlayerByLastMatch(left: Player, right: Player): number {
    if (!left.lastMatchTime && !right.lastMatchTime) {
      return 0;
    }
    if (!left.lastMatchTime) {
      return 1;
    }
    if (!right.lastMatchTime) {
      return -1;
    }
    return right.lastMatchTime.getTime() - left.lastMatchTime.getTime();
  }
}
