import {Component, Input, OnInit} from '@angular/core';
import {NgOptimizedImage} from "@angular/common";

@Component({
  standalone: true,
  selector: 'app-spoud-avatar',
  templateUrl: './spoud-avatar.component.html',
  imports: [
    NgOptimizedImage
  ],
  styleUrls: ['./spoud-avatar.component.css']
})
export class SpoudAvatarComponent implements OnInit {

  public imageSrc?: string;

  constructor() {
  }

  ngOnInit() {
  }

  @Input()
  public set email(email: string) {
    this.imageSrc = `https://spoud.io/avatarservice/${email}.png`;
  }

  public error() {
    this.imageSrc = 'assets/avatar.png';
  }
}
