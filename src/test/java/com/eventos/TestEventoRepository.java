package com.eventos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.eventos.model.Evento;
import com.eventos.repository.EventoRepository;



@RunWith(SpringRunner.class)
//informa que deverá usar o próprio banco que foi configurado no application.properties
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
public class TestEventoRepository {
	
	@Autowired
	private EventoRepository repository;
	private Evento evento;
	private List<Evento> lista;

	@Test
//	@Ignore
	public void testSave() {

		//test ok
		 assertNotNull(repository.save(new Evento("evento teste", "local teste", "10/04/2019", "11:00")));
		 
		 //test error
		 assertNotNull(repository.save(new Evento(null, null, "10/04/2019", "11:00")));
	}
	
	@Test
//	@Ignore
	public void testFindById() {
		
		//test ok
		assertNotNull(repository.findEventById(1));
		
		//teste error/falha
		assertNotNull(repository.findEventById(6));
		
	}

	@Test 
//	@Ignore
	public void testUpdate() {
		
		//test ok
		evento = repository.findEventById(1);
		evento.setNome("Teste Junit");
		assertNotNull(repository.save(evento));
		
		//test error
		evento = repository.findEventById(5);
		evento.setNome(null);
		assertNotNull(repository.save(evento));
	}
	
	@Test
//	@Ignore
	public void testDelete() {
		
		//test ok
		evento = repository.findEventById(1);
		repository.delete(evento);
		assertNotNull(evento);
		
		//test erro
		evento = repository.findEventById(-1);
		repository.delete(evento);
		assertNotNull(evento);
	}
	
	@Test
//	@Ignore
	public void testList() {
		
		//test ok
	    assertNotNull( repository.findAll() );
	    
	    //erro
	    assertEquals(lista, repository.findAll());
		
		
	}
}
