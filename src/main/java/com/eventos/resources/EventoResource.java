package com.eventos.resources;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
	public Evento addEvent(@RequestBody @Valid Evento evento) {
		return er.save(evento);
	}
	
	@PostMapping(path = "/addText")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Response<Evento>> addText(@RequestBody String evento) {
		System.out.println(evento + "recebido");
		Response<Evento> response = new Response<>();
		
		if(evento.equals("")) {
			response.getErrors().add("Event void");
			return ResponseEntity.badRequest().body(response);
		}
		
		JsonObject jsonObject = new JsonParser().parse(evento).getAsJsonObject();
		String nome = jsonObject.get("nome").toString();
		String local = jsonObject.get("local").toString();
		String horario = jsonObject.get("horario").toString();
		String data = jsonObject.get("data").toString();
		
		if (!isValid(nome) && !isValid(local) && !isValid(horario) && !isValid(data)) {
			Evento e = new Evento(nome, local, horario, data);
			if(!er.save(e).equals(null)) {
				response.setData(e);
				return ResponseEntity.ok(response);
			}
		}
		
		response.getErrors().add("Error while trying to save");
		return ResponseEntity.badRequest().body(response);
	}
	
	private boolean isValid(String valor) {
		return (valor.isEmpty() || valor.trim().isEmpty());
	}

	@DeleteMapping()
	public Evento deleteEvent(@RequestBody Evento evento) {
		er.delete(evento);
		return evento;
	}
	
	@PutMapping(path = "/updateText")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Response<Evento>> updateText(@RequestBody String evento) {
		System.out.println(evento + "recebido");
		Response<Evento> response = new Response<>();
		
		if(evento.equals("")) {
			response.getErrors().add("Event void");
			return ResponseEntity.badRequest().body(response);
		}
		
		JsonObject jsonObject = new JsonParser().parse(evento).getAsJsonObject();
		Integer id = Integer.parseInt(jsonObject.get("id_evento").toString());
		String nome = jsonObject.get("nome").toString();
		String local = jsonObject.get("local").toString();
		String horario = jsonObject.get("horario").toString();
		String data = jsonObject.get("data").toString();
		
		Evento e;
		if(id > 0 ) {
			e = er.findEventById(id);
			if(e != null) {
			
				if (!isValid(nome) && !isValid(local) && !isValid(horario) && !isValid(data)) {
					e = new Evento(nome, local, horario, data);
					if(!er.save(e).equals(null)) {
						response.setData(e);
						return ResponseEntity.ok(response);
						
					}response.getErrors().add("Error while trying to save.");
					return ResponseEntity.badRequest().body(response);
					
				}response.getErrors().add("Fields invalid.");
				return ResponseEntity.badRequest().body(response);
				
			}response.getErrors().add("Evento not found.");
			return ResponseEntity.badRequest().body(response);
			
		}
		
		response.getErrors().add("ID invalid");
		return ResponseEntity.badRequest().body(response);
	}

//	@DeleteMapping(path = "/{id}")
//	public ResponseEntity<Evento> deleteById(@PathVariable("id") Integer id) {
//
//		Evento e = er.findEventById(id);
//		System.out.println(id + "recebido" + e.toString());
//		return new ResponseEntity<Evento>(e, HttpStatus.OK);
//		
//	}
}
