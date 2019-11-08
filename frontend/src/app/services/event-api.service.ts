import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {MatchEO} from '../entities/match';

@Injectable()
export class EventApiService {

  public matchStream(): Observable<MatchEO> {
    return this.createSSEObervable('/api/v1/sse/matches');
  }

  public scoreChangeStream(): Observable<any> {
    return this.createSSEObervable('/api/v1/sse/scores');
  }

  private createSSEObervable<T>(url: string): any {
    const observable = new Observable<T>(observer => {
      const eventSource = new EventSource(url);
      eventSource.onmessage = x => observer.next(JSON.parse(x.data));
      eventSource.onerror = x => observer.error(x);

      return () => {
        eventSource.close();
      };
    });

    return observable;
  }

}
