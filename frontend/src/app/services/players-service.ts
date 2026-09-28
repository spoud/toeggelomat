import {inject, Injectable, Signal, signal} from "@angular/core";
import {Player} from "../../generated/graphql";
import {
  AllPlayersDocument,
  ArchivedPlayersDocument,
  CreatePlayerDocument,
  DeletePlayerDocument,
  PlayerStatsDocument,
  SeasonRankingDocument,
  UnarchivePlayerDocument
} from "../../generated/graphql-operations";
import {Apollo} from "apollo-angular";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class PlayersService {

  private apollo = inject(Apollo);

  private _players = signal<Player[]>([]);
  private _archivedPlayers = signal<Player[]>([]);

  constructor() {
    this.reloadPlayers();
  }

  public reloadPlayers(): void {
    this.apollo.query({query: AllPlayersDocument})
      .pipe(
        map(res => res.data?.allPlayers as Player[]),
        map(list => list
          .slice()
          .sort((l, r) => (r.defensePoints + r.offensePoints) - (l.defensePoints + l.offensePoints)))
      )
      .subscribe(this._players.set);
  }

  public fetchSeasonRanking(seasonUuid: string) {
    return this.apollo.query({query: SeasonRankingDocument, variables: {seasonUuid}})
      .pipe(map(res => res.data?.seasonRanking as Player[]));
  }

  public fetchPlayerStats(seasonUuid: string) {
    return this.apollo.query({query: PlayerStatsDocument, variables: {seasonUuid}})
      .pipe(map(res => res.data?.playerStats ?? []));
  }

  public reloadArchivedPlayers(): void {
    this.apollo.query({query: ArchivedPlayersDocument})
      .pipe(map(res => res.data?.archivedPlayers as Player[]))
      .subscribe(this._archivedPlayers.set);
  }

  public createPlayer(nickName: string): void {
    this.apollo.mutate({mutation: CreatePlayerDocument, variables: {nickName}})
      .subscribe(() => this.reloadPlayers());
  }

  public deletePlayer(uuid: string): void {
    this.apollo.mutate({mutation: DeletePlayerDocument, variables: {uuid}})
      .subscribe(() => {
        this.reloadPlayers();
        this.reloadArchivedPlayers();
      });
  }

  public unarchivePlayer(uuid: string): void {
    this.apollo.mutate({mutation: UnarchivePlayerDocument, variables: {uuid}})
      .subscribe(() => {
        this.reloadPlayers();
        this.reloadArchivedPlayers();
      });
  }

  get players(): Signal<Player[]> {
    return this._players;
  }

  get archivedPlayers(): Signal<Player[]> {
    return this._archivedPlayers;
  }
}
