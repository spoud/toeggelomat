import {Component, computed, effect, inject, signal} from '@angular/core';
import {PercentPipe} from "@angular/common";
import {PlayersService} from "../services/players-service";
import {SeasonsService} from "../services/seasons-service";
import {SeasonSelectorComponent} from "../season-selector/season-selector.component";
import {PlayerStats} from "../../generated/graphql";

@Component({
  selector: 'app-stats',
  templateUrl: './stats.component.html',
  styleUrl: './stats.component.scss',
  imports: [
    SeasonSelectorComponent,
    PercentPipe
  ]
})
export class StatsComponent {

  private playersService = inject(PlayersService);
  private seasonsService = inject(SeasonsService);

  public hasSeasons = computed(() => this.seasonsService.seasons().length > 0);
  public selectedSeasonUuid = signal<string | undefined>(undefined);

  private stats = signal<PlayerStats[]>([]);

  public sortedStats = computed(() =>
    this.stats()
      .slice()
      .sort((l, r) => (r.offensePoints + r.defensePoints) - (l.offensePoints + l.defensePoints)));

  constructor() {
    effect(() => {
      const seasonUuid = this.selectedSeasonUuid();
      if (seasonUuid) {
        this.playersService.fetchPlayerStats(seasonUuid).subscribe(stats => this.stats.set(stats));
      } else {
        this.stats.set([]);
      }
    });
  }
}
