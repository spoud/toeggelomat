import {Component, input, OnInit} from '@angular/core';
import {Player} from "../../generated/graphql";

// inspired by https://github.com/8Tesla8/empty-avatar-photo
@Component({
    selector: 'app-spoud-avatar',
    templateUrl: './spoud-avatar.component.html',
    styleUrls: ['./spoud-avatar.component.css']
})
export class SpoudAvatarComponent implements OnInit {

  public player = input.required<Player>();

  public initials: string = '';
  public circleColor: string = '';

  private colors = [
    '#e6194b',
    '#3cb44b',
    '#ffe119',
    '#4363d8',
    '#f58231',
    '#911eb4',
    '#f032e6',
    '#008080',
    '#9a6324',
    '#800000',
    '#808000',
    '#ffd8b1',
    '#000075',
    '#808080',
    '#000000'
  ];

  ngOnInit() {
    let index;
    const p = this.player();
    if (p) {
      this.initials = p.nickName.slice(0, 2);
      index = this.hashCode(p.nickName) % this.colors.length;
    } else {
      index = Math.floor(Math.random() * Math.floor(this.colors.length));
    }

    this.circleColor = this.colors[index];
  }

  private hashCode(str: string): number {
    let hash = 0;
    let i, chr;
    if (str.length === 0) return hash;
    for (i = 0; i < str.length; i++) {
      chr = str.charCodeAt(i);
      hash = ((hash << 5) - hash) + chr;
      hash |= 0; // Convert to 32bit integer
    }
    return Math.abs(hash);
  }
}
