import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BikesComponent } from './bike/bikes.component';

@Component({
  imports: [RouterModule, BikesComponent],
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'angular-ssr';
}
