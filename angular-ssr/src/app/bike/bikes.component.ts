import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Bike } from './bike';

@Component({
  selector: 'app-bikes',
  imports: [CommonModule],
  templateUrl: './bikes.component.html',
  styleUrl: './bikes.component.css',
})
export class BikesComponent implements OnInit{
test() {
console.log("HELLO")
}

  bikes: Bike[] = [];
  constructor(private http: HttpClient){

  }
  ngOnInit(): void {
    this.http.get<Bike[]>("http://localhost:8080/bikes").subscribe(bike => {
      this.bikes = [...bike];
    })
  }

  addToCart(bikeId: number) {
    console.log("HELLO")
    this.http.post("http://localhost:8080/cart/add/"+bikeId, {}).subscribe(success=>{
      console.log(success)
    })  
  }

}
