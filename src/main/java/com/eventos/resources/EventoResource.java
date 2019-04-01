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
	
	//receive format text
	@PostMapping(path = "/addOrUpdateText")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Response<Evento>> addOrUpdateText(@RequestBody String evento) {

		Response<Evento> response = new Response<>();
		
		if(evento.equals("")) {
			response.getErrors().add("Event void");
			return ResponseEntity.badRequest().body(response);
		}
		
		JsonObject jsonObject = new JsonParser().parse(evento).getAsJsonObject();
		Integer id = Integer.parseInt(jsonObject.get("id").toString());
		String nome = replaceValue(jsonObject.get("nome").toString());
		String local = replaceValue(jsonObject.get("local").toString());
		String horario = replaceValue(jsonObject.get("horario").toString());
		String data = replaceValue(jsonObject.get("data").toString());
		Evento ev;

		if(id != 0) {
			
			id = Integer.parseInt(jsonObject.get("id").toString());
			ev = er.findEventById(id);
			
			if(!ev.equals(null)) {
				ev.setNome(nome);
				ev.setLocal(local);
				ev.setHorario(horario);
				ev.setData(data);
				
				if(!er.save(ev).equals(null)) {
					response.setData(ev);
					return ResponseEntity.ok(response);
				}
			}
		}

		else if (!isValid(nome) && !isValid(local) && !isValid(horario) && !isValid(data)) {
			Evento e = new Evento(nome, local, horario, data);
			if(!er.save(e).equals(null)) {
				response.setData(e);
				return ResponseEntity.ok(response);
			}
		}
		
		response.getErrors().add("Error while trying to save");
		return ResponseEntity.badRequest().body(response);
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

	private boolean isValid(String valor) {
		return (valor.isEmpty() || valor.trim().isEmpty());
	}
	
	private String replaceValue(String value) {
		return value.replace("\"", "");
	}

}
