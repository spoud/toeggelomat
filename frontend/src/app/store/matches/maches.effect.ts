import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {EMPTY} from 'rxjs';
import {catchError, map, mergeMap, withLatestFrom} from 'rxjs/operators';
import {matchStarted, startMatch} from './maches.actions';
import {MatchesApiService} from '../../services/matches-api.service';
import {Store} from '@ngrx/store';
import {GlobalStore} from '../global';
import {MatchEO} from '../../entities/match';
import {MatchWithPlayers} from './matches.reducer';
import {Router} from '@angular/router';

@Injectable()
export class MatchesEffect {

  startMatch$ = createEffect(() => this.actions$.pipe(
    ofType(startMatch),
    withLatestFrom(this.store),
    mergeMap((arr: [any, GlobalStore]) => {
      return this.matchesApiService.startMatch(arr[0].playerUuids).pipe(
        map((match: MatchEO) => {
          console.log('Start match');
          const players = arr[1].players.list;
          const matchWithPlayer: MatchWithPlayers = new MatchWithPlayers(match);
          matchWithPlayer.playerBlueDefense = players.find(p => p.uuid === match.playerBlueDefenseUuid);
          matchWithPlayer.playerBlueOffense = players.find(p => p.uuid === match.playerBlueOffenseUuid);
          matchWithPlayer.playerRedDefense = players.find(p => p.uuid === match.playerRedDefenseUuid);
          matchWithPlayer.playerRedOffense = players.find(p => p.uuid === match.playerRedOffenseUuid);

          this.router.navigate([`current-match`]);
          return matchStarted({match: matchWithPlayer});
        }),
        catchError(() => EMPTY)
      );
    })
  ));

  constructor(
    private actions$: Actions,
    private matchesApiService: MatchesApiService,
    private store: Store<GlobalStore>,
    private router: Router
  ) {
  }
}
