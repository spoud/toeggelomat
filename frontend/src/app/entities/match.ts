import {PlayerEO} from './playersl';
import {GlobalStore} from '../store/global';

export class MatchEO {

  public uuid: string;

  public matchTime: Date;

  public redScore: number;

  public blueScore: number;

  public points: number;

  public playerRedDefenseUuid: string;

  public playerRedOffenseUuid: string;

  public playerBlueDefenseUuid: string;

  public playerBlueOffenseUuid: string;
}


export class MatchWithPlayers {

  constructor(public match: MatchEO) {
  }

  public playerRedDefense: PlayerEO;

  public playerRedOffense: PlayerEO;

  public playerBlueDefense: PlayerEO;

  public playerBlueOffense: PlayerEO;


  static createMatchWithPlayer(match: MatchEO, players: PlayerEO[]): MatchWithPlayers {
    const matchWithPlayer: MatchWithPlayers = new MatchWithPlayers(match);
    matchWithPlayer.playerBlueDefense = players.find(p => p.uuid === match.playerBlueDefenseUuid);
    matchWithPlayer.playerBlueOffense = players.find(p => p.uuid === match.playerBlueOffenseUuid);
    matchWithPlayer.playerRedDefense = players.find(p => p.uuid === match.playerRedDefenseUuid);
    matchWithPlayer.playerRedOffense = players.find(p => p.uuid === match.playerRedOffenseUuid);
    return matchWithPlayer;
  }

}
