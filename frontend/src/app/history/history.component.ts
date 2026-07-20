import {Component, computed, effect, inject, signal} from '@angular/core';
import {LastMatchesComponent} from "../scoreboard/last-matches/last-matches.component";
import {PlayersScoreboardComponent} from "../scoreboard/players-scoreboard/players-scoreboard.component";
import {SeasonsService} from "../services/seasons-service";

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrl: './history.component.scss',
  imports: [
    LastMatchesComponent,
    PlayersScoreboardComponent
  ]
})
export class HistoryComponent {

  private seasonsService = inject(SeasonsService);

  public seasons = this.seasonsService.seasons;
  public selectedSeasonUuid = signal<string | undefined>(undefined);
  public hasSeasons = computed(() => this.seasons().length > 0);

  constructor() {
    // There's no meaningful "all seasons combined" ranking (live points are
    // just the current season's, since they reset each season), so this
    // page always shows one specific season's data - default to the active
    // one, or the most recent if none is active, rather than leaving it
    // unselected.
    effect(() => {
      if (this.selectedSeasonUuid() === undefined) {
        const seasons = this.seasonsService.seasons();
        const defaultSeason = seasons.find(s => !s.endTime) ?? seasons[0];
        if (defaultSeason) {
          this.selectedSeasonUuid.set(defaultSeason.uuid);
        }
      }
    });
  }

  public onSeasonFilterChange(event: Event): void {
    this.selectedSeasonUuid.set((event.target as HTMLSelectElement).value);
  }
}
