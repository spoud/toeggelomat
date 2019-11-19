import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {Injectable} from '@angular/core';
import {map} from 'rxjs/operators';
import {environment} from '../../environments/environment';
import {MatchEO} from '../entities/match';

@Injectable()
export class MatchesApiService {

  private readonly headers = new HttpHeaders({
    'Content-Type': 'application/json',
  });

  constructor(private http: HttpClient) {
  }

  public startMatch(playerUuids: string[]): Observable<MatchEO> {
    if (environment.mock) {
      return this.mockMatch();
    }

    return this.http
      .post<MatchEO>(`/api/v1/matches/randomize`, playerUuids, {
        observe: 'response',
        headers: this.headers
      })
      .pipe(
        map((res: HttpResponse<MatchEO>) => res.body)
      );
  }

  public saveScore(match: MatchEO): Observable<MatchEO> {
    if (environment.mock) {
      return this.mockMatch();
    }

    return this.http
      .post<MatchEO>(`/api/v1/matches/set-score`, match, {
        observe: 'response',
        headers: this.headers
      })
      .pipe(
        map((res: HttpResponse<MatchEO>) => res.body)
      );
  }

  public getLastMaches(): Observable<MatchEO[]> {
    if (environment.mock) {
      return of([]);
    }

    return this.http
      .get<MatchEO[]>(`/api/v1/matches`, {
        observe: 'response'
      })
      .pipe(
        map((res: HttpResponse<MatchEO[]>) => res.body),
        map(list => list.map((m: MatchEO) => {
          m.matchTime = new Date(m.matchTime);
          return m;
        })),
        map(list => list.sort((l, r) => r.matchTime.getTime() - l.matchTime.getTime()))
      );
  }

  private mockMatch(): Observable<MatchEO> {
    return of(
      {
        uuid: '65691fb6-0056-4a78-b3ea-2aa58130ea98',
        matchTime: null,
        redScore: null,
        blueScore: null,
        points: null,
        potentialRedPoints: 30,
        potentialBluePoints: 10,
        playerRedDefenseUuid: '65691fb6-0056-4a78-b3ea-2aa58130ea98',
        playerRedOffenseUuid: 'cc07cf47-59db-4e3b-b430-a3deacc5e603',
        playerBlueDefenseUuid: '3b01e01f-56f4-48a8-aad1-b0db90444498',
        playerBlueOffenseUuid: '9e021eb1-9bcf-4e66-a4a4-e5e28cd55414'
      });
  }
}
