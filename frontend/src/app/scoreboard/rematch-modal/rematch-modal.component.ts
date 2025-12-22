import {Component, inject, ViewChild} from '@angular/core';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {Match} from "../../../generated/graphql";
import {MatchDisplayComponent} from "../../match-display/match-display.component";
import {Score} from "../../utils/types";
import {Router} from "@angular/router";
import {MatchesService} from "../../services/matches-service";

@Component({
  selector: 'app-rematch-modal',
  imports: [
    MatchDisplayComponent
  ],
  templateUrl: './rematch-modal.component.html',
  styleUrl: './rematch-modal.component.scss',
})
export class RematchModalComponent {

  private modalService = inject(NgbModal);
  private router = inject(Router);
  private matchesService = inject(MatchesService);

  @ViewChild('content')
  private content: any;

  public match?: Match;
  public score: Score = new Score();

  public rematch(match: Match): void {
    this.match = match;
    this.score = Score.fromMatch(match);

    this.modalService.open(this.content, {size: 'lg'}).result.then((result) => {
      switch (result) {
        case 'same':
          this.matchesService.rematch(match);
          break;
        case 'random':
          this.matchesService.startMatch([
            match.redTeam.defensePlayer.uuid,
            match.redTeam.offensePlayer.uuid,
            match.blueTeam.defensePlayer.uuid,
            match.blueTeam.offensePlayer.uuid
          ]);
          break;
      }
    });

  }

}
