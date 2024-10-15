package com.desafio.outsera.dto;

import java.util.List;

public record MovieDTO(
	    int year,
	    String title,
	    List<String> studios,
	    List<String> producersNames,
	    boolean winner) {}
