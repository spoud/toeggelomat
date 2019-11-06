import {createAction, props} from '@ngrx/store';
import {MatchEO} from '../../entities/match';
import {MatchWithPlayers} from './matches.reducer';

export const startMatch = createAction('[Match] start', props<{ playerUuids: string[] }>());
export const matchStarted = createAction('[Match] started', props<{ match: MatchWithPlayers }>());
export const saveMatchScore = createAction('[Match] saveScore', props<{ match: MatchEO }>());
