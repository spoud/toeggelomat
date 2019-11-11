import { SubscriptionLike } from 'rxjs';

export class SubscriptionHelper {
  private subscriptions: SubscriptionLike[] = [];

  public addSubscription(sub: SubscriptionLike): void {
    this.subscriptions.push(sub);
  }

  public unsubscribeAll(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
    this.subscriptions = [];
  }
}
