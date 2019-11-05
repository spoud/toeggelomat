import {MatchesState} from './matches/matches.reducer';
import {PlayersState} from './players/players.reducer';

export class GlobalStore {
  public players: PlayersState;
  public matches: MatchesState;
}
