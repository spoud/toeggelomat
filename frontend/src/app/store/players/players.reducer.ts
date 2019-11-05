import {PlayersState} from './players.model';
import {Action, createReducer, on} from '@ngrx/store';
import {playersLoaded} from './players.action';

export const initialState: PlayersState = new PlayersState();
initialState.list = [];

export function playerReducer(s: PlayersState | undefined, a: Action) {
  return createReducer(initialState,
    on(playersLoaded, (state: PlayersState, { players }) => ({
      ...state,
      list: players
    })),
  )(s, a);
}
