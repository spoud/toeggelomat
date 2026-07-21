import {Component, effect, inject, model} from '@angular/core';
import {SeasonsService} from "../services/seasons-service";

@Component({
  selector: 'app-season-selector',
  templateUrl: './season-selector.component.html',
  imports: []
})
export class SeasonSelectorComponent {

  private seasonsService = inject(SeasonsService);

  public seasons = this.seasonsService.seasons;
  public selectedSeasonUuid = model<string | undefined>(undefined);

  constructor() {
    // There's no meaningful "all seasons combined" view for season-scoped
    // data (live points are just the current season's, since they reset
    // each season), so callers always get one specific season selected -
    // the active one, or the most recent if none is active - rather than
    // leaving it unselected.
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
