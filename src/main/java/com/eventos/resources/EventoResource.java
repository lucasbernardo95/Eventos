package com.eventos.resources;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.model.Evento;
import com.eventos.repository.EventoRepository;

//Responsável por fornecer os recursos quando solicitado
@RestController
@RequestMapping("/evento")
public class EventoResource {
	
	@Autowired
	private EventoRepository er;
	
	//Retorna a lista de eventos da base de dados
	@GetMapping(produces = "application/json")
	public @ResponseBody Iterable<Evento> listEvents() {
		Iterable<Evento> listEvents = er.findAll();
		return listEvents;
	}
	
	//Se o JSON não tiver o atributo id é cadastro, caso contrário é alteração
	@PostMapping()
	public Evento addEvent(@RequestBody @Valid Evento evento) {
		return er.save(evento);
	}
	
	@DeleteMapping()
	public Evento deleteEvent(@RequestBody Evento evento) {
		er.delete(evento);
		return evento;
	}
}
