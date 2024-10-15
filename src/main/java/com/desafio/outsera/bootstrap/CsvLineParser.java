package com.desafio.outsera.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.desafio.outsera.dto.MovieDTO;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CsvLineParser {

    private static final Logger logger = LoggerFactory.getLogger(CsvLineParser.class);

	
    private static final int REQUIRED_FIELD_COUNT = 4; // year, title, studios, producers (winner é opcional)
    private static final String PRODUCER_SPLIT_REGEX = ",|\\s*and\\s*";
    private static final String STUDIO_SPLIT_REGEX = ",|\\s*and\\s*";

    public Optional<MovieDTO> parseLine(String[] fields, int lineNumber) {
        if (isInvalidLine(fields)) {
            logger.warn("Linha {} ignorada por dados incompletos.", lineNumber);
            return Optional.empty();
        }

        try {
            int year = parseYear(fields[0], lineNumber);
            String title = fields[1].trim();
            String studiosField = fields[2];
            String producersField = fields[3];
            List<String> studios = parseStudios(studiosField);
            List<String> producersNames = parseProducers(producersField);
            String winnerField = fields.length > 4 ? fields[4] : "";
            boolean winner = parseWinner(winnerField);

            MovieDTO movieData = new MovieDTO(year, title, studios, producersNames, winner);
            return Optional.of(movieData);

        } catch (NumberFormatException e) {
            logger.error("Erro ao converter o ano na linha {}: {}", lineNumber, e.getMessage());
            return Optional.empty();
        }
    }

    private boolean isInvalidLine(String[] fields) {
        return fields.length < REQUIRED_FIELD_COUNT ||
               fields[0].trim().isEmpty() ||
               fields[1].trim().isEmpty() ||
               fields[2].trim().isEmpty() ||
               fields[3].trim().isEmpty();
    }

    private int parseYear(String yearField, int lineNumber) {
        try {
            int year = Integer.parseInt(yearField.trim());
            int currentYear = LocalDateTime.now().getYear();

            if (year > currentYear) {
                logger.error("Erro na linha {}: Ano de lançamento superior ao ano atual.", lineNumber);
                throw new NumberFormatException("Ano inválido");
            }

            return year;
        } catch (NumberFormatException e) {
            logger.error("Erro ao converter o ano na linha {}: {}", lineNumber, e.getMessage());
            throw e;
        }
    }

    private List<String> parseStudios(String studiosField) {
        return Arrays.stream(studiosField.split(STUDIO_SPLIT_REGEX))
                     .map(String::trim)
                     .filter(name -> !name.isEmpty())
                     .collect(Collectors.toList());
    }

    private List<String> parseProducers(String producersField) {
        return Arrays.stream(producersField.split(PRODUCER_SPLIT_REGEX))
                     .map(String::trim)
                     .filter(name -> !name.isEmpty())
                     .collect(Collectors.toList());
    }

    private boolean parseWinner(String winnerField) {
        return "yes".equalsIgnoreCase(winnerField.trim());
    }
}
