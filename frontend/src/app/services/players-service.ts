import {Injectable, Signal, signal} from "@angular/core";
import {AllPlayersGQL, Player} from "../../generated/graphql";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class PlayersService {

  private _players = signal<Player[]>([]);

  constructor(private allPlayerGql: AllPlayersGQL) {
    this.reloadPlayers();
  }

  public reloadPlayers(): void {
    this.allPlayerGql.fetch()
      .pipe(
        map(res => res.data?.allPlayers as Player[]),
        map(list => list
          .slice()
          .sort((l, r) => (r.defensePoints + r.offensePoints) - (l.defensePoints + l.offensePoints)))
      )
      .subscribe(this._players.set);
  }

  get players(): Signal<Player[]> {
    return this._players;
  }
}
