package com.desafio.outsera.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.desafio.outsera.dto.AwardIntervalDTO;
import com.desafio.outsera.model.Producer;
import com.desafio.outsera.repository.MovieRepository;
import com.desafio.outsera.repository.ProducerRepository;

@Service
public class AwardService {

    private final MovieRepository movieRepository;
    private final ProducerRepository producerRepository;

    public AwardService(MovieRepository movieRepository, ProducerRepository producerRepository) {
        this.movieRepository = movieRepository;
        this.producerRepository = producerRepository;
    }

    public List<AwardIntervalDTO> getAwardIntervals() {
        List<Producer> producers = producerRepository.findAll();
        List<AwardIntervalDTO> intervals = new ArrayList<>();

        for (Producer producer : producers) {
            intervals.addAll(calculateIntervalsForProducer(producer));
        }

        return intervals;
    }

    private List<AwardIntervalDTO> calculateIntervalsForProducer(Producer producer) {
        List<AwardIntervalDTO> intervals = new ArrayList<>();
        List<Integer> winningYears = movieRepository.findWinnersYearsByProducer(producer.getId());
        Collections.sort(winningYears);

        if (winningYears.size() > 1) {
            for (int i = 0; i < winningYears.size() - 1; i++) {
                int previousWin = winningYears.get(i);
                int followingWin = winningYears.get(i + 1);
                int interval = followingWin - previousWin;

                intervals.add(new AwardIntervalDTO(producer.getName(), interval, previousWin, followingWin));
            }
        }

        return intervals;
    }

    public Map<String, List<AwardIntervalDTO>> getMinMaxIntervals() {
        List<AwardIntervalDTO> intervals = getAwardIntervals();

        int minInterval = findMinInterval(intervals);
        int maxInterval = findMaxInterval(intervals);

        List<AwardIntervalDTO> minIntervalProducers = filterIntervalsByValue(intervals, minInterval);
        List<AwardIntervalDTO> maxIntervalProducers = filterIntervalsByValue(intervals, maxInterval);

        Map<String, List<AwardIntervalDTO>> result = new HashMap<>();
        result.put("min", minIntervalProducers);
        result.put("max", maxIntervalProducers);

        return result;
    }

    private int findMinInterval(List<AwardIntervalDTO> intervals) {
        return intervals.stream()
                .mapToInt(AwardIntervalDTO::interval)
                .min()
                .orElse(0);
    }

    private int findMaxInterval(List<AwardIntervalDTO> intervals) {
        return intervals.stream()
                .mapToInt(AwardIntervalDTO::interval)
                .max()
                .orElse(0);
    }

    private List<AwardIntervalDTO> filterIntervalsByValue(List<AwardIntervalDTO> intervals, int value) {
        return intervals.stream()
                .filter(dto -> dto.interval() == value)
                .collect(Collectors.toList());
    }
}
