import {Component, inject, ViewChild} from '@angular/core';
import {ScoreConfirmationModalComponent} from '../score-confirmation-modal/score-confirmation-modal.component';

import {Router, RouterModule} from "@angular/router";
import {MatchesService} from "../services/matches-service";

@Component({
  selector: 'app-current-match',
  templateUrl: './current-match.component.html',
  styleUrls: ['./current-match.component.css'],
  imports: [
    RouterModule,
    ScoreConfirmationModalComponent
  ]
})
export class CurrentMatchComponent {

  private matchesService = inject(MatchesService);

  public currentMatch = this.matchesService.currentMatch;
  private router = inject(Router);

  public blueScoreList: number[] = [8, 7, 6, 5, 4, 3, 2, 1, 0];
  public redScoreList: number[] = [0, 1, 2, 3, 4, 5, 6, 7, 8];
  public blueScore = -1;
  public redScore = -1;

  @ViewChild('confirm')
  private confirmDialog?: ScoreConfirmationModalComponent;

  public saveScore(): void {
    let match = this.currentMatch();
    if (match) {
      const match = Object.assign({}, this.currentMatch());
      match.blueScore = this.blueScore;
      match.redScore = this.redScore;
      this.confirmDialog?.confirmMatchResult(match, () => {
        this.matchesService.saveScore({
          blueScore: this.blueScore,
          redScore: this.redScore,
          playerBlueDefenseUuid: match.blueTeam.defensePlayer.uuid,
          playerBlueOffenseUuid: match.blueTeam.offensePlayer.uuid,
          playerRedDefenseUuid: match.redTeam.defensePlayer.uuid,
          playerRedOffenseUuid: match.redTeam.offensePlayer.uuid
        });
        this.router.navigate(['scoreboard']);
      });
    }
  }

  public autoFill(): void {
    if (this.blueScore === -1 && this.redScore !== 7) {
      this.blueScore = 7;
    }
    if (this.redScore === -1 && this.blueScore !== 7) {
      this.redScore = 7;
    }
  }
}
