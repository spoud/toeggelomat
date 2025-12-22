import {Match} from "../../generated/graphql";

const NO_SCORE_SELECTED = -1;

export class Score {

  constructor(public readonly blueScore: number = NO_SCORE_SELECTED, public readonly redScore: number = NO_SCORE_SELECTED) {
  }

  public isValid(): boolean {
    return this.blueScore !== NO_SCORE_SELECTED && this.redScore !== NO_SCORE_SELECTED;
  }

  public redScoreSelected(score: number): Score {
    if (this.blueScore === NO_SCORE_SELECTED && score !== 7) {
      // auto select 7 for blue
      return new Score(7, score);
    } else {
      return new Score(this.blueScore, score);
    }
  }

  public blueScoreSelected(score: number): Score {
    if (this.redScore === NO_SCORE_SELECTED && score !== 7) {
      // auto select 7 for red
      return new Score(score, 7);
    } else {
      return new Score(score, this.redScore);
    }
  }

  public static fromMatch(match: Match): Score {
    return new Score(match.blueScore || NO_SCORE_SELECTED, match.redScore || NO_SCORE_SELECTED);
  }
}

export enum TeamColor {
  BLUE = 'blue',
  RED = 'red'
}
