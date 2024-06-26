package com.larissa.tcc2024.repository;

import com.larissa.tcc2024.model.ParametroAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParametroAgendaRepository extends JpaRepository<ParametroAgenda, UUID> {
}
