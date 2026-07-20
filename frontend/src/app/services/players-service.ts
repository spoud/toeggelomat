import {inject, Injectable, Signal, signal} from "@angular/core";
import {AllPlayersGQL, CreatePlayerGQL, DeletePlayerGQL, Player} from "../../generated/graphql";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class PlayersService {

  private allPlayerGql = inject(AllPlayersGQL);
  private createPlayerGql = inject(CreatePlayerGQL);
  private deletePlayerGql = inject(DeletePlayerGQL);

  private _players = signal<Player[]>([]);

  constructor() {
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

  public createPlayer(nickName: string): void {
    this.createPlayerGql.mutate({variables: {nickName}})
      .subscribe(() => this.reloadPlayers());
  }

  public deletePlayer(uuid: string): void {
    this.deletePlayerGql.mutate({variables: {uuid}})
      .subscribe(() => this.reloadPlayers());
  }

  get players(): Signal<Player[]> {
    return this._players;
  }
}
