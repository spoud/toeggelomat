import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType, ROOT_EFFECTS_INIT} from '@ngrx/effects';
import {PlayersApiService} from '../../services/players-api.service';
import {EMPTY} from 'rxjs';
import {playersLoaded, playersReload} from './players.action';
import {catchError, debounceTime, map, mergeMap} from 'rxjs/operators';
import {EventApiService} from '../../services/event-api.service';

@Injectable()
export class PlayersEffect {
  initPlayers = createEffect(() =>
    this.actions$.pipe(
      ofType(ROOT_EFFECTS_INIT),
      map(() => playersReload())
    )
  );

  scoreStreams = createEffect(() =>
    this.actions$.pipe(
      ofType(ROOT_EFFECTS_INIT),
      debounceTime(500),
      mergeMap(() => this.eventApiService.scoreChangeStream().pipe(
        map(() => playersReload())
      ))
    )
  );

  loadPlayers$ = createEffect(() => this.actions$.pipe(
    ofType(playersReload),
    mergeMap(() => this.playerApiService.getAll()
      .pipe(
        map(players => playersLoaded({players})),
        catchError(() => EMPTY)
      ))
    )
  );

  constructor(
    private actions$: Actions,
    private playerApiService: PlayersApiService,
    private eventApiService: EventApiService
  ) {
  }
}
