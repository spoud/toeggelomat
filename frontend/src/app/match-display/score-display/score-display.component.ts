import {Component, computed, HostBinding, input, output} from '@angular/core';
import {TeamColor} from "../../utils/types";

@Component({
  selector: 'app-score-display',
  imports: [],
  templateUrl: './score-display.component.html',
  styleUrl: './score-display.component.scss',
})
export class ScoreDisplayComponent {

  editable = input<boolean>(false);
  score = input<number>();
  color = input.required<TeamColor>();
  scoreSelected = output<number>();

  scoreList = computed(() => {
    if (this.color() == TeamColor.RED) {
      return [0, 1, 2, 3, 4, 5, 6, 7, 8];
    } else {
      return [8, 7, 6, 5, 4, 3, 2, 1, 0];
    }
  })

  protected clickOnScore(s: number) {
    if(this.editable()) {
      this.scoreSelected.emit(s);
    }
  }

  protected readonly TeamColor = TeamColor;

}
