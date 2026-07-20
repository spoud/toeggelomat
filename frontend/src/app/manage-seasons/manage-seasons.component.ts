import {Component, inject, signal} from '@angular/core';
import {DatePipe} from "@angular/common";
import {SeasonsService} from "../services/seasons-service";

@Component({
  selector: 'app-manage-seasons',
  templateUrl: './manage-seasons.component.html',
  styleUrl: './manage-seasons.component.scss',
  imports: [
    DatePipe
  ]
})
export class ManageSeasonsComponent {

  private seasonsService = inject(SeasonsService);

  public seasons = this.seasonsService.seasons;
  public newSeasonLabel = signal('');

  public onNewSeasonLabelInput(event: Event): void {
    this.newSeasonLabel.set((event.target as HTMLInputElement).value);
  }

  public startSeason(): void {
    const label = this.newSeasonLabel().trim();
    if (label) {
      this.seasonsService.createSeason(label);
      this.newSeasonLabel.set('');
    }
  }

  public closeSeason(uuid: string): void {
    this.seasonsService.closeSeason(uuid);
  }
}
