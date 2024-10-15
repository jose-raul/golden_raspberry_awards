package com.desafio.outsera.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desafio.outsera.model.Producer;

public interface ProducerRepository extends JpaRepository<Producer, Long> {

    Optional<Producer> findByName(String trimProducerName);

}
