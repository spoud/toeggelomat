import {Component, computed, effect, inject, input, ViewChild} from '@angular/core';
import {CommonModule, DatePipe} from "@angular/common";
import {MatchesService} from "../../services/matches-service";
import {SeasonsService} from "../../services/seasons-service";
import {Match, Team} from "../../../generated/graphql";
import {RematchModalComponent} from "../rematch-modal/rematch-modal.component";

export class MatchWithWinnerLooser {
  match: Match;

  winners: Team;
  loosers: Team;
  score: string;

  constructor(match: Match) {
    this.match = match;
    if (match.blueScore && match.redScore && match.blueScore > match.redScore) {
      this.winners = match.blueTeam;
      this.loosers = match.redTeam;
      this.score = `${match.blueScore}-${match.redScore}`;
    } else {
      this.winners = match.redTeam;
      this.loosers = match.blueTeam;
      this.score = `${match.redScore}-${match.blueScore}`;
    }
  }
}

@Component({
  selector: 'app-last-matches',
  templateUrl: './last-matches.component.html',
  styleUrls: ['./last-matches.component.css'],
  imports: [
    CommonModule,
    DatePipe,
    RematchModalComponent
  ]
})
export class LastMatchesComponent {
  private matchesService = inject(MatchesService);
  private seasonsService = inject(SeasonsService);

  public mode = input<'current' | 'browse'>('browse');

  @ViewChild('rematchRef')
  private rematchModal?: RematchModalComponent;

  public matches = computed(() => {
    return this.matchesService.lastMatches().map(m => new MatchWithWinnerLooser(m));
  });

  public seasons = this.seasonsService.seasons;
  public showSeasonFilter = computed(() => this.mode() === 'browse');

  constructor() {
    effect(() => {
      if (this.mode() === 'current') {
        this.matchesService.filterBySeason(this.seasonsService.activeSeason()?.uuid);
      } else {
        this.matchesService.filterBySeason(undefined);
      }
    });
  }

  public onSeasonFilterChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.matchesService.filterBySeason(value ? value : undefined);
  }

  public rematch(match: Match) {
    this.rematchModal?.rematch(match);
  }

}
