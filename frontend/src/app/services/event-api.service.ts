import {Observable, ReplaySubject, Subject} from 'rxjs';
import {Injectable, NgZone} from '@angular/core';
import {MatchEO} from '../entities/match';

@Injectable()
export class EventApiService {

  private scoreChanges: Subject<any>;
  private newMatch: Subject<MatchEO>;

  public constructor(private zone: NgZone) {
    this.scoreChanges = this.createSSEObervable('/api/v1/sse/scores');
    // this.newMatch = this.createSSEObervable('/api/v1/sse/matches');
  }

  // public matchStream(): Observable<MatchEO> {
  // }

  public scoreChangeStream(): Observable<any> {
    return this.scoreChanges;
  }

  private createSSEObervable<T>(url: string): Subject<T> {
    const subject:Subject<T> = new ReplaySubject<T>(1);

    const eventSource = new EventSource(url);
    eventSource.onmessage = event => this.zone.run(() => {
      subject.next(JSON.parse(event.data));
    });
    eventSource.onerror = error => this.zone.run(() => {
      subject.error(error);
      debugger;
    });

    return subject;
  }

}
