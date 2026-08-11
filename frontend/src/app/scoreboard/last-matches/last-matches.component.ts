import {Component, computed, effect, inject, input, ViewChild} from '@angular/core';
import {CommonModule, DatePipe} from "@angular/common";
import {MatchesService} from "../../services/matches-service";
import {Match, Team} from "../../../generated/graphql";
import {RematchModalComponent} from "../rematch-modal/rematch-modal.component";

export class MatchWithWinnerLoser {
  match: Match;

  winners: Team;
  losers: Team;
  score: string;

  constructor(match: Match) {
    this.match = match;
    if ((match.blueScore ?? 0) > (match.redScore ?? 0)) {
      this.winners = match.blueTeam;
      this.losers = match.redTeam;
      this.score = `${match.blueScore}-${match.redScore}`;
    } else {
      this.winners = match.redTeam;
      this.losers = match.blueTeam;
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

  public seasonUuid = input<string | undefined>(undefined);

  @ViewChild('rematchRef')
  private rematchModal?: RematchModalComponent;

  public matches = computed(() => {
    return this.matchesService.lastMatches().map(m => new MatchWithWinnerLoser(m));
  });

  constructor() {
    effect(() => {
      this.matchesService.filterBySeason(this.seasonUuid());
    });
  }

  public rematch(match: Match) {
    this.rematchModal?.rematch(match);
  }

}
