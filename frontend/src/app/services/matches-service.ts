import {inject, Injectable, Signal, signal} from "@angular/core";
import {Match, SaveScoreInput} from "../../generated/graphql";
import {LastMatchesDocument, SaveScoreDocument, StartMatchDocument} from "../../generated/graphql-operations";
import {Apollo} from "apollo-angular";
import {map} from "rxjs/operators";
import {Router} from "@angular/router";
import {PlayersService} from "./players-service";

@Injectable({
  providedIn: 'root'
})
export class MatchesService {

  private apollo = inject(Apollo);
  private router = inject(Router);
  private playersService = inject(PlayersService);

  private _lastMatches = signal<Match[]>([]);
  private _currentMatch = signal<Match | undefined>(undefined);
  private seasonUuid: string | undefined = undefined;

  public constructor() {
    this.reloadMatches();
  }

  public reloadMatches(): void {
    // Guard against out-of-order responses: switching seasons quickly (or the
    // initial undefined -> active-season transition) can have an earlier,
    // larger request (e.g. the unscoped "all seasons" fetch) resolve after a
    // later, smaller one, clobbering it with stale/wrongly-scoped data.
    const requestedSeasonUuid = this.seasonUuid;
    this.apollo.query({query: LastMatchesDocument, variables: {seasonUuid: requestedSeasonUuid}})
      .pipe(
        map(res => res.data?.lastMatches as Match[]),
        map(list => list.slice().sort((l, r) => (r.matchTime?.getTime() ?? 0) - (l.matchTime?.getTime() ?? 0)))
      )
      .subscribe(list => {
        if (this.seasonUuid === requestedSeasonUuid) {
          this._lastMatches.set(list);
        }
      });
  }

  public filterBySeason(seasonUuid: string | undefined): void {
    this.seasonUuid = seasonUuid;
    this.reloadMatches();
  }

  startMatch(playerUuids: string[]) {
    this.apollo.mutate({
      mutation: StartMatchDocument,
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
    this.apollo.mutate({
      mutation: SaveScoreDocument,
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
