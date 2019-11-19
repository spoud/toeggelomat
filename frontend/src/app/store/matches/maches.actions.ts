import {createAction, props} from '@ngrx/store';
import {MatchEO} from '../../entities/match';

export const startMatch = createAction('[Match] start', props<{ playerUuids: string[] }>());
export const matchStarted = createAction('[Match] started', props<{ match: MatchEO }>());
export const saveMatchScore = createAction('[Match] saveScore', props<{ match: MatchEO }>());
export const matchesReload = createAction('[Match] reload');
export const matchesReloaded = createAction('[Match] reloaded', props<{ matches: MatchEO[] }>());
