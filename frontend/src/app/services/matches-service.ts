import {inject, Injectable, Signal, signal} from "@angular/core";
import {LastMatchesGQL, Match, SaveScoreGQL, SaveScoreInput, StartMatchGQL} from "../../generated/graphql";
import {map} from "rxjs/operators";
import {Router} from "@angular/router";
import {PlayersService} from "./players-service";

@Injectable({
  providedIn: 'root'
})
export class MatchesService {

  private lastMatchesGql = inject(LastMatchesGQL);
  private startMatchGQL = inject(StartMatchGQL);
  private saveScoreGQL = inject(SaveScoreGQL);
  private router = inject(Router);
  private playersService = inject(PlayersService);

  private _lastMatches = signal<Match[]>([]);
  private _currentMatch = signal<Match | undefined>(undefined);
  private seasonUuid: string | undefined = undefined;

  public constructor() {
    this.reloadMatches();
  }

  public reloadMatches(): void {
    this.lastMatchesGql.fetch({variables: {seasonUuid: this.seasonUuid}})
      .pipe(
        map(res => res.data?.lastMatches as Match[]),
        map(list => list.slice().sort((l, r) => r.matchTime.getTime() - l.matchTime.getTime()))
      )
      .subscribe(this._lastMatches.set);
  }

  public filterBySeason(seasonUuid: string | undefined): void {
    this.seasonUuid = seasonUuid;
    this.reloadMatches();
  }

  startMatch(playerUuids: string[]) {
    this.startMatchGQL.mutate({
      variables: {
        playerUuids
      }
    })
      .pipe(
        map(res => res.data?.randomizeMatch as Match),
      )
      .subscribe(m => {
        this._currentMatch.set(m);
        this.router.navigate(['current-match']);
      })
  }

  rematch(match:Match) {
    this._currentMatch.set({
      ...match,
      uuid: "",
      blueScore: 0,
      redScore: 0,
    });
    this.router.navigate(['current-match']);
  }

  saveScore(scores: SaveScoreInput) {
    this.saveScoreGQL.mutate({
      variables: {
        scores
      }
    })
      .subscribe(() => {
        // clear current match
        this._currentMatch.set(undefined);
        this.reloadMatches();
        this.playersService.reloadPlayers();
      })
  }

  get lastMatches(): Signal<Match[]> {
    return this._lastMatches;
  }

  get currentMatch(): Signal<Match | undefined> {
    return this._currentMatch;
  }
}
