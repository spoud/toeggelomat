import {Component, computed, effect, inject, input, signal} from '@angular/core';
import {SpoudAvatarComponent} from "../../spoud-avatar/spoud-avatar.component";
import {LastMatchTimePipe} from "./last-match-time.pipe";
import {PlayersService} from "../../services/players-service";
import {Player} from "../../../generated/graphql";


@Component({
  selector: 'app-players-scoreboard',
  templateUrl: './players-scoreboard.component.html',
  styleUrls: ['./players-scoreboard.component.css'],
  imports: [
    SpoudAvatarComponent,
    LastMatchTimePipe
  ]
})
export class PlayersScoreboardComponent {

  private playersService = inject(PlayersService);

  public seasonUuid = input<string | undefined>(undefined);

  private seasonRanking = signal<Player[]>([]);

  public players = computed(() =>
    this.seasonRanking()
      .slice()
      .sort((l, r) => (r.defensePoints + r.offensePoints) - (l.defensePoints + l.offensePoints))
  );

  constructor() {
    effect(() => {
      const seasonUuid = this.seasonUuid();
      if (seasonUuid) {
        this.playersService.fetchSeasonRanking(seasonUuid).subscribe(players => this.seasonRanking.set(players));
      }
    });
  }

}
