import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType, ROOT_EFFECTS_INIT} from '@ngrx/effects';
import {PlayersApiService} from '../../services/players-api.service';
import {EMPTY} from 'rxjs';
import {playersLoaded, reloadPlayers} from './players.action';
import {catchError, map, mergeMap} from 'rxjs/operators';

@Injectable()
export class PlayersEffect {
  initPlayers = createEffect(() =>
    this.actions$.pipe(
      ofType(ROOT_EFFECTS_INIT),
      map(() => reloadPlayers())
    )
  );

  loadPlayers$ = createEffect(() => this.actions$.pipe(
    ofType(reloadPlayers),
    mergeMap(() => this.playerApiService.getAll()
      .pipe(
        map(players => playersLoaded({players})),
        catchError(() => EMPTY)
      ))
    )
  );

  constructor(
    private actions$: Actions,
    private playerApiService: PlayersApiService
  ) {
  }
}
