package com.desafio.outsera.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.desafio.outsera.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT m.year FROM Movie m JOIN m.producers p WHERE p.id = :producerId AND m.winner = true")
    List<Integer> findWinnersYearsByProducer(@Param("producerId") Long producerId);

    Optional<Movie> findByTitleAndYear(String title, int year);

}
