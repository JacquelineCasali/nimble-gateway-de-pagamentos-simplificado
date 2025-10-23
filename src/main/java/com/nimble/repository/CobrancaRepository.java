package com.nimble.repository;

import com.nimble.entity.Status;
import com.nimble.entity.User;
import com.nimble.entity.Cobranca;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CobrancaRepository extends JpaRepository<Cobranca,Long> {


   List<Cobranca> findByOriginadorAndStatus(User originador, Status status);
   List<Cobranca> findByDestinatarioAndStatus(User destinatario, Status status);


}
