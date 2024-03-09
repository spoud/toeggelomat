import {MatchEO} from '../../entities/match';
import {Action, createReducer, on} from '@ngrx/store';
import {matchesReloaded, matchStarted} from './maches.actions';

export class MatchesState {
  public currentMatch?: MatchEO;
  public lastMatches: MatchEO[] = [];
}

const initialState = new MatchesState();
initialState.currentMatch = undefined;
initialState.lastMatches = [];

export function machesReducer(s: MatchesState | undefined, a: Action) {
  return createReducer(initialState,
    on(matchStarted, (state: MatchesState, {match}) => ({
      ...state,
      currentMatch: match
    })),
    on(matchesReloaded, (state: MatchesState, {matches}) => ({
      ...state,
      lastMatches: matches
    })),
  )(s, a);
}
