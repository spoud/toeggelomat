import {createAction, props} from '@ngrx/store';
import {PlayerEO} from '../../entities/playersl';

export const reloadPlayers = createAction('[Players] reload');
export const playersLoaded = createAction('[Players] loaded', props<{ players: PlayerEO[] }>());


