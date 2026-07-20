import {inject, Injectable, Signal, signal} from "@angular/core";
import {AllSeasonsGQL, CloseSeasonGQL, CreateSeasonGQL, Season} from "../../generated/graphql";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class SeasonsService {

  private allSeasonsGql = inject(AllSeasonsGQL);
  private createSeasonGql = inject(CreateSeasonGQL);
  private closeSeasonGql = inject(CloseSeasonGQL);

  private _seasons = signal<Season[]>([]);

  constructor() {
    this.reloadSeasons();
  }

  public reloadSeasons(): void {
    this.allSeasonsGql.fetch()
      .pipe(map(res => res.data?.allSeasons as Season[]))
      .subscribe(this._seasons.set);
  }

  public createSeason(label: string): void {
    this.createSeasonGql.mutate({variables: {label}})
      .subscribe(() => this.reloadSeasons());
  }

  public closeSeason(uuid: string): void {
    this.closeSeasonGql.mutate({variables: {uuid}})
      .subscribe(() => this.reloadSeasons());
  }

  get seasons(): Signal<Season[]> {
    return this._seasons;
  }
}
