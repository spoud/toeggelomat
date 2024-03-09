import {PlayerEO} from './players';

export interface MatchEO {

  uuid: string;

  matchTime: Date;

  redScore: number;

  blueScore: number;

  points: number;

  playerRedDefenseUuid: string;

  playerRedOffenseUuid: string;

  playerBlueDefenseUuid: string;

  playerBlueOffenseUuid: string;

  potentialBluePoints: number;

  potentialRedPoints: number;
}


export class MatchWithPlayers {

  constructor(public match: MatchEO) {
  }

  public playerRedDefense?: PlayerEO;

  public playerRedOffense?: PlayerEO;

  public playerBlueDefense?: PlayerEO;

  public playerBlueOffense?: PlayerEO;

  static createMatchWithPlayer(match: MatchEO, players: PlayerEO[]): MatchWithPlayers {
    const matchWithPlayer: MatchWithPlayers = new MatchWithPlayers(match);
    matchWithPlayer.playerBlueDefense = players.find(p => p.uuid === match.playerBlueDefenseUuid);
    matchWithPlayer.playerBlueOffense = players.find(p => p.uuid === match.playerBlueOffenseUuid);
    matchWithPlayer.playerRedDefense = players.find(p => p.uuid === match.playerRedDefenseUuid);
    matchWithPlayer.playerRedOffense = players.find(p => p.uuid === match.playerRedOffenseUuid);
    return matchWithPlayer;
  }

}
