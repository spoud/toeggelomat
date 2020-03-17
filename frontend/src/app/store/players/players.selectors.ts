import {createSelector} from '@ngrx/store';
import {GlobalStore} from '../global';
import {PlayersState} from './players.reducer';
import {MatchesState} from '../matches/matches.reducer';

export const playersFeature = (state: GlobalStore) => state.players;
export const matchFeature = (state: GlobalStore) => state.matches;

export const selectPlayersList = createSelector(
  playersFeature,
  (state: PlayersState) => state.list
);


export const selectLastMatches = createSelector(
  matchFeature,
  (state: MatchesState) => state.lastMatches
);

export const selectCurrentMatch = createSelector(
  matchFeature,
  (state: MatchesState) => state.currentMatch
);

