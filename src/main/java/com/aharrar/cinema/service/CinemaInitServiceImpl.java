package com.aharrar.cinema.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aharrar.cinema.dao.CategorieRepository;
import com.aharrar.cinema.dao.CinemaRepository;
import com.aharrar.cinema.dao.FilmRepository;
import com.aharrar.cinema.dao.PlaceRepository;
import com.aharrar.cinema.dao.ProjectionRepository;
import com.aharrar.cinema.dao.SalleRepository;
import com.aharrar.cinema.dao.SeanceRepository;
import com.aharrar.cinema.dao.TicketRepository;
import com.aharrar.cinema.dao.VilleRepository;
import com.aharrar.cinema.entities.Categorie;
import com.aharrar.cinema.entities.Cinema;
import com.aharrar.cinema.entities.Film;
import com.aharrar.cinema.entities.Place;
import com.aharrar.cinema.entities.Projection;
import com.aharrar.cinema.entities.Salle;
import com.aharrar.cinema.entities.Seance;
import com.aharrar.cinema.entities.Ticket;
import com.aharrar.cinema.entities.Ville;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CinemaInitServiceImpl implements ICinemaInitService{
	
	@Autowired
	private VilleRepository villeRepository;
	
	@Autowired
	private CinemaRepository cinemaRepository;
	
	@Autowired
	private SalleRepository salleRepository;
	
	@Autowired
	private PlaceRepository placeRepository;
	
	@Autowired
	private SeanceRepository seanceRepository;
	
	@Autowired
	private FilmRepository filmRepository;
	
	@Autowired
	private ProjectionRepository projectionRepository;
	
	@Autowired
	private CategorieRepository categorieRepository;
	
	@Autowired
	private TicketRepository ticketRepository;
	

	@Override
	public void initVilles() {
		Stream.of("Casablanca","Marrakech","Rabat","Tanger").forEach(nameVile ->{
			Ville ville = new Ville();
			ville.setName(nameVile);
			villeRepository.save(ville);
		});
		
	}

	@Override
	public void initCinemas() {
		villeRepository.findAll().forEach(v ->{
			Stream.of("Magarama","UGC","Imax","Kinepolis")
			.forEach(nameCinema ->{
				Cinema cinema = new Cinema();
				cinema.setName(nameCinema);
				cinema.setNombreSalles(3+(int)(Math.random()*7));
				cinema.setVille(v);
				cinemaRepository.save(cinema);
			});
		});
		
	}

	@Override
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema ->{
			for(int i = 0;i < cinema.getNombreSalles();i++) {
				Salle salle = new Salle();
				salle.setName("Salle" + (i+1));
				salle.setCinema(cinema);
				salle.setNombrePlace(15 +(int)(Math.random()*20));
				salleRepository.save(salle);
			}
		});
		
	}

	@Override
	public void initSeances() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Stream.of("12:00","15:00","17:00","19:00","21:00")
			.forEach(s ->{
				Seance seance = new Seance();
				try {
					seance.setHeureDebut(dateFormat.parse(s));
					seanceRepository.save(seance);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			});
	}

	@Override
	public void initPlaces() {
		salleRepository.findAll().forEach(salle ->{
			for(int i = 0;i< salle.getNombrePlace();i++) {
				Place place = new Place();
				place.setNumero(i+1);
				place.setSalle(salle);
				placeRepository.save(place);
			}
		});
		
	}

	@Override
	public void initCategories() {
		Stream.of("Histoire","Action","Fiction","Drame")
			.forEach(c ->{
				Categorie categorie = new Categorie();
				categorie.setName(c);
				categorieRepository.save(categorie);
			});
		
	}

	@Override
	public void initFilms() {
		double[] durees = new double[] {1,1.5,2,2.5,3};
		List<Categorie> categories = categorieRepository.findAll();
		Stream.of("Matrix","Mission-impossible","Armagedon","le-messager","volubilis","le-Gardien")
			.forEach(titreFilm ->{
				Film film = new Film();
				film.setTitre(titreFilm);
				film.setDuree(durees[new Random().nextInt(durees.length)]);
				film.setPhoto(titreFilm.replace(" ","")+".jpg");
				film.setCategorie(categories.get(new Random().nextInt(categories.size())));
				filmRepository.save(film);
			});			
	}

	@Override
	public void initProjections() {
		double[] prices = new double[] {30,50,60,70,90,100};
		villeRepository.findAll().forEach(ville ->{
			ville.getCinemas().forEach(cinema ->{
				cinema.getSalles().forEach(salle ->{
					filmRepository.findAll().forEach(film->{
						seanceRepository.findAll().forEach(seance ->{
							Projection projection = new Projection();
							projection.setDateProjection(new Date());
							projection.setFilm(film);
							projection.setPrix(prices[new Random().nextInt(prices.length)]);
							projection.setSalle(salle);
							projection.setSeance(seance);
							projectionRepository.save(projection);
						});
					});
				});
			});
		});
		
	}

	@Override
	public void initTickets() {
		projectionRepository.findAll().forEach(p->{
			p.getSalle().getPlaces().forEach(place->{
				Ticket ticket = new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(p.getPrix());
				ticket.setProjection(p);
				ticket.setReserve(false);
				ticketRepository.save(ticket);
			});
		});
	}

}
