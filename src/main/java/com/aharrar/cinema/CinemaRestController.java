package com.aharrar.cinema;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aharrar.cinema.dao.FilmRepository;
import com.aharrar.cinema.dao.TicketRepository;
import com.aharrar.cinema.entities.Film;
import com.aharrar.cinema.entities.Ticket;

import jakarta.transaction.Transactional;
import lombok.Data;


@RestController
@CrossOrigin("*")
public class CinemaRestController {
	
	@Autowired
	private FilmRepository filmRepository;
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@GetMapping(path = "/imageFilm/{id}",produces = org.springframework.http.MediaType.IMAGE_JPEG_VALUE)
	public byte[] image(@PathVariable(name = "id")Long id) throws Exception {
		Film f = filmRepository.findById(id).get();
		String photoName = f.getPhoto();
		File file = new File(System.getProperty("user.home") + "/cinema/images/" + photoName);
		Path path = Paths.get(file.toURI());
		return Files.readAllBytes(path);
	}
	
	@PostMapping("/payerTickets")
	@Transactional
	public List<Ticket> payerTickets(@RequestBody TicketForm ticketForm){
		List<Ticket> listTickets = new ArrayList<Ticket>();
		ticketForm.getTickets().forEach(idTicket ->{
			System.out.println(idTicket);
			Ticket ticket = ticketRepository.findById(idTicket).get();
			ticket.setNomClient(ticketForm.getNomClient());
			ticket.setReserve(true);
			ticketRepository.save(ticket);
			listTickets.add(ticket);
		});
		return listTickets;
	}
	
}

@Data
class TicketForm{
	private String nomClient;
	private int codePayement;
	private List<Long> tickets = new ArrayList<Long>();
}
