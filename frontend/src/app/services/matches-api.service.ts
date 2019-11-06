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
      .post<MatchEO>(`/api/v1/matches`, playerUuids, {
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
      .post<MatchEO>(`/api/v1/matches/finish`, match, {
        observe: 'response',
        headers: this.headers
      })
      .pipe(
        map((res: HttpResponse<MatchEO>) => res.body)
      );
  }

  private mockMatch(): Observable<MatchEO> {
    return of(
      {
        uuid: '65691fb6-0056-4a78-b3ea-2aa58130ea98',
        resultTime: null,
        createdTime: null,
        redScore: null,
        blueScore: null,
        point: null,
        playerRedDefenseUuid: '65691fb6-0056-4a78-b3ea-2aa58130ea98',
        playerRedOffenseUuid: 'cc07cf47-59db-4e3b-b430-a3deacc5e603',
        playerBlueDefenseUuid: '3b01e01f-56f4-48a8-aad1-b0db90444498',
        playerBlueOffenseUuid: '9e021eb1-9bcf-4e66-a4a4-e5e28cd55414'
      });
  }
}
