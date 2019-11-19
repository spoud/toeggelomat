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
}
