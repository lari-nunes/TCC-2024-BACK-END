package com.larissa.tcc2024.controller;

import com.larissa.tcc2024.exceptions.AgendamentoExistenteException;
import com.larissa.tcc2024.model.Agenda;
import com.larissa.tcc2024.model.Endereco;
import com.larissa.tcc2024.model.Pessoa;
import com.larissa.tcc2024.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/agenda")
public class AgendaController {
    @Autowired
    private AgendaService agendaService;

    @PostMapping
    public ResponseEntity<Object> gravarAgenda(@RequestBody Agenda agenda) {
        try {
            LocalDateTime dataAtual = LocalDateTime.now();
            LocalDateTime dataAgendamento = agenda.getDataAgendamento();

            // Verifica se a data de agendamento é anterior a data atual
            if (dataAgendamento.isBefore(dataAtual)) {
                return ResponseEntity.badRequest().body("Data indisponível.");
            }

            // Verifica se já existe um agendamento no mesmo horário
            agendaService.verificarAgendamentoExistente(dataAgendamento);

            // Salva o novo agendamento
            Agenda agendaSalva = agendaService.gravarAgenda(agenda);
            return ResponseEntity.status(HttpStatus.CREATED).body(agendaSalva);
        } catch (AgendamentoExistenteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/listarAgendamentos/{id}")
    public ResponseEntity<Object> listarAgendamentos(@PathVariable UUID id){
        List<Agenda> agendas = agendaService.listarAgendas(id);
        if(agendas.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não existe essa agenda");
        }
        return ResponseEntity.status(HttpStatus.OK).body(agendas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> agendaPorId(@PathVariable (value = "id") UUID id){
        Optional<Agenda> agenda = agendaService.buscarAgendaId(id);
        if(agenda.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não existe essa agenda");
        }
        return ResponseEntity.status(HttpStatus.OK).body(agendaService.buscarAgendaId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarAgendaId(@PathVariable(value = "id") UUID id, @RequestBody Agenda agenda) {

        try {
            Optional<Agenda> agenda1 = agendaService.atualizarAgendaId(id);

            if (agenda1.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body("Agenda não encontrada");
            }

            Agenda agenda2 = agenda1.get();
            agenda2.setStatus_agendamento(agenda.getStatus_agendamento());
            agenda2.setIn_disponivel(agenda.getIn_disponivel());
            agenda2.setObservacao(agenda.getObservacao());
            agenda2.setDataAgendamento(agenda.getDataAgendamento());
            agenda2.setServico(agenda.getServico());
            agenda2.setPessoa(agenda.getPessoa());

            Agenda agendaAtualizada = agendaService.gravarAgenda(agenda2);
            return ResponseEntity.status(HttpStatus.OK).body(agendaAtualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar a requisição");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarAgendaId(@PathVariable(value = "id") UUID id) {
        try {
            Optional<Agenda> agenda = agendaService.buscarAgendaId(id);

            if (agenda.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body("Não localizado");
            }

            agendaService.deletarAgendaId(agenda);

            return ResponseEntity.status(HttpStatus.OK).body("Deletado com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar a requisição");
        }
    }
}
