import {Component, computed, inject} from '@angular/core';
import {PlayersScoreboardComponent} from "./players-scoreboard/players-scoreboard.component";
import {LastMatchesComponent} from "./last-matches/last-matches.component";
import {SeasonsService} from "../services/seasons-service";

import {RouterModule} from "@angular/router";

@Component({
  selector: 'app-scoreboard',
  templateUrl: './scoreboard.component.html',
  styleUrls: ['./scoreboard.component.css'],
  imports: [
    RouterModule,
    PlayersScoreboardComponent,
    LastMatchesComponent
  ]
})
export class ScoreboardComponent {

  private seasonsService = inject(SeasonsService);

  public heading = computed(() => {
    const label = this.seasonsService.activeSeason()?.label;
    return label ? `Score board (${label})` : 'Score board';
  });

  public hasActiveSeason = computed(() => !!this.seasonsService.activeSeason());
  public activeSeasonUuid = computed(() => this.seasonsService.activeSeason()?.uuid);
}
