import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {PlayerEO} from '../entities/playersl';
import {Injectable} from '@angular/core';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class PlayersApiService {
  constructor(private http: HttpClient) {
  }

  public getAll(): Observable<PlayerEO[]> {
    return this.http
      .get<PlayerEO[]>(`/api/v1/players`, {
        observe: 'response'
      })
      .pipe(
        map((res: HttpResponse<PlayerEO[]>) => res.body),
        map(list => list.map((p: PlayerEO) => {
          p.lastMatchTime = p.lastMatchTime && new Date(p.lastMatchTime);
          return p;
        })),
      );

  }
}
