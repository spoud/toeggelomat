import {ChangeDetectionStrategy, Component, computed, effect, inject, ViewChild} from '@angular/core';
import {CommonModule, DatePipe} from "@angular/common";
import {MatchesService} from "../../services/matches-service";
import {Match, Team} from "../../../generated/graphql";
import {RematchModalComponent} from "../rematch-modal/rematch-modal.component";
import {ScoreConfirmationModalComponent} from "../../current-match/score-confirmation-modal/score-confirmation-modal.component";

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


  @ViewChild('rematchRef')
  private rematchModal?: RematchModalComponent;

  public matches = computed(() => {
    return this.matchesService.lastMatches().map(m => new MatchWithWinnerLooser(m));
  });

  public rematch(match: Match) {
    this.rematchModal?.rematch(match);
  }

}
