import {Component, inject, signal} from '@angular/core';
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

  public onSeasonFilterChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.selectedSeasonUuid.set(value ? value : undefined);
  }
}
