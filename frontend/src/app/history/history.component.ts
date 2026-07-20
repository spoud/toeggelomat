import {Component, computed, inject, signal} from '@angular/core';
import {LastMatchesComponent} from "../scoreboard/last-matches/last-matches.component";
import {PlayersScoreboardComponent} from "../scoreboard/players-scoreboard/players-scoreboard.component";
import {SeasonSelectorComponent} from "../season-selector/season-selector.component";
import {SeasonsService} from "../services/seasons-service";

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrl: './history.component.scss',
  imports: [
    LastMatchesComponent,
    PlayersScoreboardComponent,
    SeasonSelectorComponent
  ]
})
export class HistoryComponent {

  private seasonsService = inject(SeasonsService);

  public hasSeasons = computed(() => this.seasonsService.seasons().length > 0);
  public selectedSeasonUuid = signal<string | undefined>(undefined);
}
