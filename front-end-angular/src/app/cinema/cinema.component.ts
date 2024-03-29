import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { CinemaService } from '../service/cinema.service';

@Component({
  selector: 'app-cinema',
  templateUrl: './cinema.component.html',
  styleUrl: './cinema.component.css'
})
export class CinemaComponent implements OnInit{
	
	public villes;
	public cinemas;
	public currentVille;
	public currentCinema;
	public salles;
	
	constructor(public cinemaService:CinemaService){}
	
	ngOnInit() {
		
		this.cinemaService.getVilles()
			.subscribe(data =>{
					this.villes=data;
				},err =>{
					console.log(err);
				
			})
    
   }
   
	onGetCinemas(v){
		this.currentVille = v;
		this.cinemaService.getCinemas(v)
			.subscribe(data =>{
					this.cinemas=data;
				},err =>{
					console.log(err);
				
			})
	}
	
	onGetSalles(c){
		this.currentCinema = c;
		this.cinemaService.getSalles(c)
			.subscribe(data =>{
					this.salles=data;
					this.salles._embedded.salles.forEach(salle =>{
						this.cinemaService.getProjections(salle)
							.subscribe(data =>{
								salle.projections=data;
								},err=>{
									console.log(err);
							})
					})
				},err =>{
					console.log(err);
				
			})
	}

}
