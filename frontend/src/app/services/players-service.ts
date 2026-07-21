import {inject, Injectable, Signal, signal} from "@angular/core";
import {
  AllPlayersGQL,
  ArchivedPlayersGQL,
  CreatePlayerGQL,
  DeletePlayerGQL,
  Player,
  PlayerStatsGQL,
  SeasonRankingGQL,
  UnarchivePlayerGQL
} from "../../generated/graphql";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class PlayersService {

  private allPlayerGql = inject(AllPlayersGQL);
  private archivedPlayersGql = inject(ArchivedPlayersGQL);
  private createPlayerGql = inject(CreatePlayerGQL);
  private deletePlayerGql = inject(DeletePlayerGQL);
  private unarchivePlayerGql = inject(UnarchivePlayerGQL);
  private seasonRankingGql = inject(SeasonRankingGQL);
  private playerStatsGql = inject(PlayerStatsGQL);

  private _players = signal<Player[]>([]);
  private _archivedPlayers = signal<Player[]>([]);

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

  public fetchSeasonRanking(seasonUuid: string) {
    return this.seasonRankingGql.fetch({variables: {seasonUuid}})
      .pipe(map(res => res.data?.seasonRanking as Player[]));
  }

  public fetchPlayerStats(seasonUuid: string) {
    return this.playerStatsGql.fetch({variables: {seasonUuid}})
      .pipe(map(res => res.data?.playerStats ?? []));
  }

  public reloadArchivedPlayers(): void {
    this.archivedPlayersGql.fetch()
      .pipe(map(res => res.data?.archivedPlayers as Player[]))
      .subscribe(this._archivedPlayers.set);
  }

  public createPlayer(nickName: string): void {
    this.createPlayerGql.mutate({variables: {nickName}})
      .subscribe(() => this.reloadPlayers());
  }

  public deletePlayer(uuid: string): void {
    this.deletePlayerGql.mutate({variables: {uuid}})
      .subscribe(() => {
        this.reloadPlayers();
        this.reloadArchivedPlayers();
      });
  }

  public unarchivePlayer(uuid: string): void {
    this.unarchivePlayerGql.mutate({variables: {uuid}})
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
