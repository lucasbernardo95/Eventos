package com.eventos.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.springframework.beans.factory.annotation.Autowired;

import com.eventos.model.Evento;
import com.eventos.repository.EventoRepository;
import com.eventos.util.MessageUtil;

@Named
public class EventoBean implements Serializable {

	@Autowired
	private EventoRepository repository;

	private Evento selected, entity;
	private List<Evento> list;

	public EventoBean() {
	}

	@PostConstruct
	public void init() {
		this.selected = new Evento();
		this.entity = new Evento();
		this.list = new ArrayList<Evento>();
		listAll();
	}

	private boolean isCampoVazio(String valor) {
		// verifica se o valor passado é nulo ou se contém apenas um 'espaço' digitado
		return (valor.isEmpty() || valor.trim().isEmpty());
	}

	public void novo() {
		if (save(entity))
			this.entity = new Evento();
	}

	public void alterar() {
		if (selected.equals(null))
			MessageUtil.warningMensage("É necessário selecionar uma notícia.");
		else if (save(selected))
			this.selected = new Evento();
	}

	public boolean save(Evento evento) {
		try {
			// Verifica se os campos não estão vazios e salva caso estejam ok.
			if (!isCampoVazio(evento.getNome()) && !isCampoVazio(evento.getLocal())
					&& !isCampoVazio(evento.getHorario()) && !isCampoVazio(evento.getData())) {

				repository.save(evento);
				list = repository.findAll();
				return true;
			} else
				MessageUtil.warningMensage("Por favor, prencha todos os campos.");

		} catch (RuntimeException e) {
			MessageUtil.errorMessage("Erro ao tentar salvar.");
		}
		list = repository.findAll();

		return false;
	}

	public void listAll() {
		try {
			list = repository.findAll();
		} catch (RuntimeException r) {
			MessageUtil.errorMessage("Erro ao tentar listar os eventos.");
		}
	}

	public void delete() {
		try {
			repository.delete(selected);
			list.remove(selected);
			selected = new Evento();
		} catch (RuntimeException r) {
			MessageUtil.errorMessage("Erro ao tentar deletar.");
		}
	}

	public void onRowSelect(SelectEvent event) {
		selected = (Evento) event.getObject();
	}

	public void onRowUnselect(UnselectEvent event) {
		// MessageUtil.successMensage("");
	}

	public Evento getSelected() {
		return selected;
	}

	public void setSelected(Evento selected) {
		this.selected = selected;
	}

	public Evento getEntity() {
		return entity;
	}

	public void setEntity(Evento entity) {
		this.entity = entity;
	}

	public List<Evento> getList() {
		return list;
	}

	public void setList(List<Evento> list) {
		this.list = list;
	}

}