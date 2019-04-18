package com.eventos.resources;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.model.Evento;
import com.eventos.repository.EventoRepository;
import com.eventos.response.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//Responsável por fornecer os recursos quando solicitado
@RestController
@RequestMapping("/evento")
public class EventoResource {

	@Autowired
	private EventoRepository er;

	// Retorna a lista de eventos da base de dados
	@GetMapping(produces = "application/json")
	public @ResponseBody Iterable<Evento> listEvents() {
		Iterable<Evento> listEvents = er.findAll();
		return listEvents;
	}

	// Se o JSON não tiver o atributo id é cadastro, caso contrário é alteração
	@PostMapping(produces = "application/json")
	public Evento addOrUpdateEvent(@RequestBody @Valid Evento evento) {
		return er.save(evento);
	}
	
	@DeleteMapping()
	public Evento deleteEvent(@RequestBody Evento evento) {
		er.delete(evento);
		return evento;
	}
	
	@PostMapping(path = "/{id}")
	public ResponseEntity<Response<Evento>> deleteById(@PathVariable("id") Integer id) {

		Response<Evento> response = new Response<>();
		
		if(id == null || id <= 0) {
			response.getErrors().add("ID invalid");
			return ResponseEntity.badRequest().body(response);
		}
		
		Evento e;
		if(id > 0 ) {
			e = er.findEventById(id);
			if(e != null) {
				
				er.delete(e);
				response.setData(e);
				return ResponseEntity.ok(response);
				
			}response.getErrors().add("Evento not found.");
			return ResponseEntity.badRequest().body(response);
			
		}
		
		response.getErrors().add("ID invalid");
		return ResponseEntity.badRequest().body(response);
	}

}