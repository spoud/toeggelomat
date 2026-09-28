import {inject, Injectable, Signal, signal} from "@angular/core";
import {Match, SaveScoreInput} from "../../generated/graphql";
import {LastMatchesDocument, SaveScoreDocument, StartMatchDocument} from "../../generated/graphql-operations";
import {Apollo} from "apollo-angular";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {Router} from "@angular/router";
import {PlayersService} from "./players-service";

const DEFAULT_PAGE_SIZE = 20;

@Injectable({
  providedIn: 'root'
})
export class MatchesService {

  private apollo = inject(Apollo);
  private router = inject(Router);
  private playersService = inject(PlayersService);

  private _lastMatches = signal<Match[]>([]);
  private _hasMoreMatches = signal(false);
  private _currentMatch = signal<Match | undefined>(undefined);
  private seasonUuid: string | undefined = undefined;
  private pageSize: number = DEFAULT_PAGE_SIZE;
  // Bumped on every reload so responses to superseded requests are dropped.
  private generation = 0;
  private loadingMore = false;

  public constructor() {
    this.reloadMatches();
  }

  public reloadMatches(): void {
    // Guard against out-of-order responses: switching seasons quickly (or the
    // initial undefined -> active-season transition) can have an earlier,
    // larger request (e.g. the unscoped "all seasons" fetch) resolve after a
    // later, smaller one, clobbering it with stale/wrongly-scoped data.
    const generation = ++this.generation;
    this.loadingMore = false;
    this.fetchPage(0).subscribe(({matches, hasMore}) => {
      if (generation === this.generation) {
        this._lastMatches.set(matches);
        this._hasMoreMatches.set(hasMore);
      }
    });
  }

  public filterBySeason(seasonUuid: string | undefined, pageSize: number = DEFAULT_PAGE_SIZE): void {
    this.seasonUuid = seasonUuid;
    this.pageSize = pageSize;
    this.reloadMatches();
  }

  public loadMore(): void {
    if (this.loadingMore) {
      return;
    }
    this.loadingMore = true;
    const generation = this.generation;
    const loaded = this._lastMatches();
    this.fetchPage(loaded.length).subscribe({
      next: ({matches, hasMore}) => {
        if (generation !== this.generation) {
          return;
        }
        this.loadingMore = false;
        // Matches saved since the previous page shift the offsets, so skip any already shown.
        const known = new Set(loaded.map(m => m.uuid));
        this._lastMatches.set([...loaded, ...matches.filter(m => !known.has(m.uuid))]);
        this._hasMoreMatches.set(hasMore);
      },
      // Let the user retry instead of leaving "Load more" stuck.
      error: () => {
        if (generation === this.generation) {
          this.loadingMore = false;
        }
      },
    });
  }

  private fetchPage(offset: number): Observable<{matches: Match[], hasMore: boolean}> {
    const pageSize = this.pageSize;
    return this.apollo.query({query: LastMatchesDocument, variables: {seasonUuid: this.seasonUuid, limit: pageSize, offset}})
      .pipe(
        map(res => res.data?.lastMatches as Match[]),
        map(list => ({
          matches: list.slice().sort((l, r) => (r.matchTime?.getTime() ?? 0) - (l.matchTime?.getTime() ?? 0)),
          // A full page means there may be more; a short one means we've reached the end.
          hasMore: list.length === pageSize,
        }))
      );
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

  get hasMoreMatches(): Signal<boolean> {
    return this._hasMoreMatches;
  }

  get currentMatch(): Signal<Match | undefined> {
    return this._currentMatch;
  }
}
