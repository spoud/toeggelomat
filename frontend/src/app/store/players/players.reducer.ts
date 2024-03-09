import {PlayerEO} from '../../entities/players';
import {Action, createReducer, on} from '@ngrx/store';
import {playersLoaded} from './players.action';

export class PlayersState {
  public list: PlayerEO[] = [];
}

export const initialState: PlayersState = new PlayersState();
initialState.list = [];

export function playersReducer(s: PlayersState | undefined, a: Action) {
  return createReducer(initialState,
    on(playersLoaded, (state: PlayersState, { players }) => ({
      ...state,
      list: players
    })),
  )(s, a);
}
