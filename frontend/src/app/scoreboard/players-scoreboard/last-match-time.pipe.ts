import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  standalone: true,
  name: 'lastMatchTime'
})
export class LastMatchTimePipe implements PipeTransform {

  private DAYS = 24 * 3600 * 1000;
  private HOURS = 3600 * 1000;
  private MINUTES = 60 * 1000;

  transform(value: Date): string {
    if (value) {
      const now = new Date().getTime();
      const time = value.getTime();
      const diff = now - time;
      if (diff > this.DAYS) {
        return `${Math.floor(diff / this.DAYS)} days ago`;
      } else if (diff > this.HOURS) {
        return `${Math.floor(diff / this.HOURS)} hours ago`;
      } else if (diff > this.MINUTES) {
        return `${Math.floor(diff / this.MINUTES)} minutes ago`;
      } else {
        return `A few seconds ago`;
      }
    }
    return 'never';
  }

}
