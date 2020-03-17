import {MatchesState} from './matches/matches.reducer';
import {PlayersState} from './players/players.reducer';

export interface GlobalStore {
  players: PlayersState;
  matches: MatchesState;
}
