import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType, ROOT_EFFECTS_INIT} from '@ngrx/effects';
import {EMPTY} from 'rxjs';
import {catchError, map, mergeMap, withLatestFrom} from 'rxjs/operators';
import {matchStarted, saveMatchScore, startMatch} from './maches.actions';
import {MatchesApiService} from '../../services/matches-api.service';
import {Store} from '@ngrx/store';
import {GlobalStore} from '../global';
import {MatchEO} from '../../entities/match';
import {MatchWithPlayers} from './matches.reducer';
import {Router} from '@angular/router';
import {reloadPlayers} from '../players/players.action';
import {EventApiService} from '../../services/event-api.service';

@Injectable()
export class MatchesEffect {

  initMatchStream$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ROOT_EFFECTS_INIT),
      mergeMap(param => {
        return this.eventApiService.matchStream()
          .pipe(
            withLatestFrom(this.store),
            map((arr: [MatchEO, GlobalStore]) => {
              console.log('Start match from event', arr[0]);
              const matchWithPlayer = this.createMatchWithPlayer(arr[0], arr[1]);

              this.router.navigate([`current-match`]);
              return matchStarted({match: matchWithPlayer});
            }),
            catchError(() => EMPTY)
          );
      })
    )
  );

  startMatch$ = createEffect(() => this.actions$.pipe(
    ofType(startMatch),
    withLatestFrom(this.store),
    mergeMap((arr: [any, GlobalStore]) => {
      return this.matchesApiService.startMatch(arr[0].playerUuids).pipe(
        map((match: MatchEO) => {
          console.log('Start match');
          const matchWithPlayer = this.createMatchWithPlayer(match, arr[1]);

          this.router.navigate([`current-match`]);
          return matchStarted({match: matchWithPlayer});
        }),
        catchError(() => EMPTY)
      );
    })
  ));

  saveScore$ = createEffect(() => this.actions$.pipe(
    ofType(saveMatchScore),
    mergeMap((param) => {
      return this.matchesApiService.saveScore(param.match).pipe(
        map((match: MatchEO) => {
          this.router.navigate([`scoreboard`]);
          return reloadPlayers();
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

  private createMatchWithPlayer(match: MatchEO, store: GlobalStore) {
    const players = store.players.list;
    const matchWithPlayer: MatchWithPlayers = new MatchWithPlayers(match);
    matchWithPlayer.playerBlueDefense = players.find(p => p.uuid === match.playerBlueDefenseUuid);
    matchWithPlayer.playerBlueOffense = players.find(p => p.uuid === match.playerBlueOffenseUuid);
    matchWithPlayer.playerRedDefense = players.find(p => p.uuid === match.playerRedDefenseUuid);
    matchWithPlayer.playerRedOffense = players.find(p => p.uuid === match.playerRedOffenseUuid);
    return matchWithPlayer;
  }
}
