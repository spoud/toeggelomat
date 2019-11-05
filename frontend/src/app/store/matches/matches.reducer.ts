import {PlayerEO} from '../../entities/playersl';
import {MatchEO} from '../../entities/match';
import {Action, createReducer, on} from '@ngrx/store';
import {matchStarted} from './maches.actions';

export class MatchWithPlayers {

  constructor(public match: MatchEO) {
  }

  public playerRedDefense: PlayerEO;

  public playerRedOffense: PlayerEO;

  public playerBlueDefense: PlayerEO;

  public playerBlueOffense: PlayerEO;

}

export class MatchesState {
  public currentMatch: MatchWithPlayers;
}

const initialState = new MatchesState();
initialState.currentMatch = null;

export function machesReducer(s: MatchesState | undefined, a: Action) {
  return createReducer(initialState,
    on(matchStarted, (state: MatchesState, {match}) => ({
      ...state,
      currentMatch: match
    })),
  )(s, a);
}
