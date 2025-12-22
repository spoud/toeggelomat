import {Component, input, output, signal} from '@angular/core';
import {Match} from "../../generated/graphql";
import {ScoreDisplayComponent} from "./score-display/score-display.component";
import {Score, TeamColor} from "../utils/types";

@Component({
  selector: 'app-match-display',
  imports: [
    ScoreDisplayComponent
  ],
  templateUrl: './match-display.component.html',
  styleUrl: './match-display.component.scss',
})
export class MatchDisplayComponent {
  match = input.required<Match|undefined>();
  editable = input<boolean>(false);
  validScore = output<boolean>();

  score = input.required<Score>();
  scoreChange = output<Score>();

  protected readonly TeamColor = TeamColor;

  protected redScoreSelected($event: number) {
    this.scoreChange.emit(this.score().redScoreSelected($event));
  }

  protected blueScoreSelected($event: number) {
    this.scoreChange.emit(this.score().blueScoreSelected($event));
  }
}
