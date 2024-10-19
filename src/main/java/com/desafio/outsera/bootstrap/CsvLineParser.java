package com.desafio.outsera.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.desafio.outsera.dto.MovieDTO;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CsvLineParser {

    private static final Logger logger = LoggerFactory.getLogger(CsvLineParser.class);

    private static final int REQUIRED_FIELD_COUNT = 4;
    private static final String PRODUCER_SPLIT_REGEX = ",\\s+and\\s+|,\\s*|\\s+and\\s+|\\s*&\\s*";
    private static final String STUDIO_SPLIT_REGEX = ",| and ";

    public Optional<MovieDTO> parseLine(String[] fields, int lineNumber) {
        if (isInvalidLine(fields)) {
            logger.warn("Linha {} ignorada por dados incompletos.", lineNumber);
            return Optional.empty();
        }

        try {
            int year = Integer.parseInt(fields[0].trim());
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

    private List<String> parseStudios(String studiosField) {
        return Arrays.stream(studiosField.split(STUDIO_SPLIT_REGEX))
                     .map(String::trim)
                     .filter(name -> !name.isEmpty())
                     .collect(Collectors.toList());
    }

    private List<String> parseProducers(String producersField) {
        String preprocessed = preprocessProducers(producersField);
        return Arrays.stream(preprocessed.split(PRODUCER_SPLIT_REGEX))
                     .map(String::trim)
                     .filter(name -> !name.isEmpty())
                     .collect(Collectors.toList());
    }

    private String preprocessProducers(String producers) {
        // Inserir espaço antes de 'and' quando estiver colado à palavra anterior
        String preprocessed = producers.replaceAll("(?i)(\\S)(and\\b)", "$1 and");
        // Inserir espaço depois de 'and' quando estiver colado à palavra seguinte
        preprocessed = preprocessed.replaceAll("(?i)(\\band)(\\S)", "and $2");
        return preprocessed;
    }

    private boolean parseWinner(String winnerField) {
        return "yes".equalsIgnoreCase(winnerField.trim());
    }
}
