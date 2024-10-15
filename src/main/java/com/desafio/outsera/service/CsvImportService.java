package com.desafio.outsera.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.outsera.bootstrap.CsvLineParser;
import com.desafio.outsera.dto.MovieDTO;
import com.desafio.outsera.model.Movie;
import com.desafio.outsera.model.Producer;
import com.desafio.outsera.model.Studio;
import com.desafio.outsera.repository.MovieRepository;
import com.desafio.outsera.repository.ProducerRepository;
import com.desafio.outsera.repository.StudioRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

@Service
public class CsvImportService {
	
    private static final Logger logger = LoggerFactory.getLogger(CsvImportService.class);


    private static final String CSV_FILE_PATH = "/movielist.csv";
    private static final String DELIMITER = ";";

    private final MovieRepository movieRepository;
    private final ProducerRepository producerRepository;
    private final StudioRepository studioRepository;
    private final CsvLineParser csvLineParser;

    public CsvImportService(MovieRepository movieRepository,
                            ProducerRepository producerRepository,
                            StudioRepository studioRepository,
                            CsvLineParser csvLineParser) {
        this.movieRepository = movieRepository;
        this.producerRepository = producerRepository;
        this.studioRepository = studioRepository;
        this.csvLineParser = csvLineParser;
    }
    
    @Transactional
    public void importCsv() {
    	InputStream csvInputStream = getClass().getResourceAsStream(CSV_FILE_PATH);
    	
    	if (csvInputStream == null) {
    		throw new IllegalArgumentException("Arquivo CSV não pode ser encontrado");
    	}
    	importCsv(csvInputStream);
    }

    @Transactional
    public void importCsv(InputStream  csvInputStream) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(csvInputStream))) {

            reader.readLine();
            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] fields = line.split(DELIMITER);
                Optional<MovieDTO> movieDataOpt = csvLineParser.parseLine(fields, lineNumber);

                movieDataOpt.ifPresent(this::saveMovieData);
            }

            logger.info("CSV importado com sucesso!");
        } catch (IOException e) {
            logger.error("Erro ao ler o arquivo CSV: {}", e.getMessage(), e);
        }
    }

    private void saveMovieData(MovieDTO movieData) {
        Movie movie = findOrCreateMovie(movieData);
        movie = movieRepository.save(movie);

        for (String producerName : movieData.producersNames()) {
            Producer producer = findOrCreateProducer(producerName);
            movie.getProducers().add(producer);
        }

        for (String studioName : movieData.studios()) {
            Studio studio = findOrCreateStudio(studioName);
            movie.getStudios().add(studio);
        }

        movieRepository.save(movie);
        
        logger.debug("Filme '{}' ({}), vencedor: {}, salvo com sucesso.",
                movie.getTitle(), movie.getYear(), movie.isWinner());
    }

    private Movie findOrCreateMovie(MovieDTO movieData) {
        return movieRepository.findByTitleAndYear(movieData.title(), movieData.year())
                .orElseGet(() -> new Movie(movieData.title(), movieData.year(), movieData.winner()));
    }

    private Producer findOrCreateProducer(String producerName) {
        return producerRepository.findByName(producerName.trim())
                .orElseGet(() -> {
                    Producer newProducer = producerRepository.save(new Producer(producerName.trim()));
                    logger.debug("Novo produtor '{}' criado.", producerName.trim());
                    return newProducer;
                });
    }
    
    private Studio findOrCreateStudio(String studioName) {
        return studioRepository.findByName(studioName.trim())
                .orElseGet(() -> {
                    Studio newStudio = studioRepository.save(new Studio(studioName.trim()));
                    logger.debug("Novo estúdio '{}' criado.", studioName.trim());
                    return newStudio;
                });
    }
}
