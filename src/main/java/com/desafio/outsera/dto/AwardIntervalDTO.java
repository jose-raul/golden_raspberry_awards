package com.desafio.outsera.dto;

public record AwardIntervalDTO(
        String producer,
        Integer interval,
        Integer previousWin,
        Integer followingWin
) {}
