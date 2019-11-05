import {createAction, props} from '@ngrx/store';
import {PlayerEO} from '../../entities/playersl';
import {MatchEO} from '../../entities/match';
import {MatchWithPlayers} from './matches.reducer';

export const startMatch = createAction('[Match] start', props<{ playerUuids: string[] }>());
export const matchStarted = createAction('[Match] started', props<{ match: MatchWithPlayers }>());
