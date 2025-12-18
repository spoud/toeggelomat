import {Injectable, Signal, signal} from "@angular/core";
import {AllPlayersGQL, Player} from "../../generated/graphql";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class PlayersService {

  private _players =  signal<Player[]>([]);

  constructor(allPlayerGql: AllPlayersGQL) {
    allPlayerGql.fetch()
      .pipe(
        map(res => res.data?.allPlayers as Player[]),
        map(list => list
          .slice()
          // .map(p => p.lastMatchTime = p.lastMatchTime && new Date(p.lastMatchTime))
          .sort((l, r) => (r.defensePoints  + r.offensePoints) - (l.defensePoints + l.offensePoints)))
      )
      .subscribe(this._players.set);
  }

  get players():Signal<Player[]>{
    return this._players;
  }
}
