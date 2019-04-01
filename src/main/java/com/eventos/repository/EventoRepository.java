package com.eventos.repository;

import org.springframework.stereotype.Repository;

import com.eventos.model.Evento;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long>  {

	@Query(" from Evento where data = :data")
	public List<Evento> findNewsByData(@Param("data") Date data);
	
	@Query(" from Evento where id_evento = :id")
	public Evento findEventById(@Param("id") Integer id);
}