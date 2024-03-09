import {createAction, props} from '@ngrx/store';
import {PlayerEO} from '../../entities/players';

export const playersReload = createAction('[Players] reload');
export const playersLoaded = createAction('[Players] loaded', props<{ players: PlayerEO[] }>());


