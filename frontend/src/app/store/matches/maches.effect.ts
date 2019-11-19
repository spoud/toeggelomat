import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType, ROOT_EFFECTS_INIT} from '@ngrx/effects';
import {EMPTY} from 'rxjs';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {matchesReload, matchesReloaded, matchStarted, saveMatchScore, startMatch} from './maches.actions';
import {MatchesApiService} from '../../services/matches-api.service';
import {Store} from '@ngrx/store';
import {GlobalStore} from '../global';
import {MatchEO} from '../../entities/match';
import {Router} from '@angular/router';
import {playersReload} from '../players/players.action';
import {EventApiService} from '../../services/event-api.service';

@Injectable()
export class MatchesEffect {

  initLastMatches$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ROOT_EFFECTS_INIT),
      map(() => matchesReload())
    ));

  reloadMatches$ = createEffect(() =>
    this.actions$.pipe(
      ofType(matchesReload),
      mergeMap(() =>
        this.matchesApiService.getLastMaches().pipe(
          map(matches => matchesReloaded({matches})),
          catchError(() => EMPTY)
        ))
    ));

  // TODO put back when we have slack integration
  // initMatchStream$ = createEffect(() =>
  //   this.actions$.pipe(
  //     ofType(ROOT_EFFECTS_INIT),
  //     mergeMap(param => {
  //       return this.eventApiService.matchStream()
  //         .pipe(
  //           map(match => {
  //             console.log('Start match from event', match);
  //             this.router.navigate([`current-match`]);
  //             return matchStarted({match});
  //           }),
  //           catchError(() => EMPTY)
  //         );
  //     })
  //   )
  // );

  startMatch$ = createEffect(() => this.actions$.pipe(
    ofType(startMatch),
    mergeMap(param => {
      return this.matchesApiService.startMatch(param.playerUuids).pipe(
        map((match: MatchEO) => {
          this.router.navigate([`current-match`]);
          return matchStarted({match});
        }),
        catchError(() => EMPTY)
      );
    })
  ));

  saveScore$ = createEffect(() => this.actions$.pipe(
    ofType(saveMatchScore),
    mergeMap((param) => {
      return this.matchesApiService.saveScore(param.match).pipe(
        mergeMap((match: MatchEO) => {
          this.router.navigate([`scoreboard`]);
          return [playersReload(), matchesReload()];
        }),
        catchError(() => EMPTY)
      );
    })
  ));

  constructor(
    private actions$: Actions,
    private matchesApiService: MatchesApiService,
    private eventApiService: EventApiService,
    private store: Store<GlobalStore>,
    private router: Router
  ) {
  }


}
