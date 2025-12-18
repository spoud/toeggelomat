import {ChangeDetectionStrategy, Component, computed, effect, inject} from '@angular/core';
import {CommonModule, DatePipe} from "@angular/common";
import {MatchesService} from "../../services/matches-service";
import {Match, Team} from "../../../generated/graphql";

export class MatchWithWinnerLooser {

  uuid: string;
  matchTime: Date;
  points: number;
  winners: Team;
  loosers: Team;
  score: string;

  constructor(match: Match) {
    this.uuid = match.uuid;
    this.matchTime = match.matchTime;
    this.points = match.points || 0;
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
    DatePipe
  ]
})
export class LastMatchesComponent {
  private matchesService = inject(MatchesService);

  public matches = computed(() => {
    return this.matchesService.lastMatches().map(m => new MatchWithWinnerLooser(m));
  })

}
