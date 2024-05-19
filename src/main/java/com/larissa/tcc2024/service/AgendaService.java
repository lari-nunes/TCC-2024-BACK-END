package com.larissa.tcc2024.service;

import com.larissa.tcc2024.model.Agenda;
import com.larissa.tcc2024.repository.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    public Agenda gravarAgenda(Agenda agenda){
        return agendaRepository.save(agenda);
    }

    public Agenda gravarAgendaNewData(Agenda agenda) {
        LocalDateTime dataAtual = LocalDateTime.now();
        LocalDateTime dataAgendamento = agenda.getDt_agendamento();

        if (dataAgendamento.isBefore(dataAtual)) {
            throw new IllegalArgumentException("Não é possível agendar datas passadas do dia de hoje ou adiante!");
        }

        return agendaRepository.save(agenda);
    }

    public List<Agenda> listarAgendamentos(UUID id){
        return agendaRepository.listarAgendas(id);
    }

    public List<Agenda> listarAgendas (UUID id){
        return agendaRepository.findAll();
    }

    public Optional<Agenda> buscarAgendaId(UUID id){
        return agendaRepository.findById(id);
    }

    public Optional<Agenda> atualizarAgendaId(UUID id){
        return agendaRepository.findById(id);
    }

    public void deletarAgendaId(Optional<Agenda> agenda){
        agendaRepository.delete(agenda.get());
    }
}
