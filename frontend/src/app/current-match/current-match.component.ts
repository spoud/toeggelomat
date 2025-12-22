import {Component, inject, signal, ViewChild} from '@angular/core';
import {ScoreConfirmationModalComponent} from './score-confirmation-modal/score-confirmation-modal.component';

import {Router, RouterModule} from "@angular/router";
import {MatchesService} from "../services/matches-service";
import {MatchDisplayComponent} from "../match-display/match-display.component";
import {Score} from "../utils/types";

@Component({
  selector: 'app-current-match',
  templateUrl: './current-match.component.html',
  styleUrls: ['./current-match.component.css'],
  imports: [
    RouterModule,
    ScoreConfirmationModalComponent,
    MatchDisplayComponent
  ]
})
export class CurrentMatchComponent {

  private matchesService = inject(MatchesService);

  public currentMatch = this.matchesService.currentMatch;
  private router = inject(Router);
  public score = new Score();


  @ViewChild('confirm')
  private confirmDialog?: ScoreConfirmationModalComponent;

  public saveScore(): void {
    let match = this.currentMatch();
    if (match) {
      const match = Object.assign({}, this.currentMatch());
      match.blueScore = this.score.blueScore;
      match.redScore = this.score.redScore;
      this.confirmDialog?.confirmMatchResult(match, () => {
        this.matchesService.saveScore({
          blueScore: this.score.blueScore,
          redScore: this.score.redScore,
          playerBlueDefenseUuid: match.blueTeam.defensePlayer.uuid,
          playerBlueOffenseUuid: match.blueTeam.offensePlayer.uuid,
          playerRedDefenseUuid: match.redTeam.defensePlayer.uuid,
          playerRedOffenseUuid: match.redTeam.offensePlayer.uuid
        });
        this.router.navigate(['scoreboard']);
      });
    }
  }

}
