package com.desafio.outsera.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.outsera.dto.AwardIntervalDTO;
import com.desafio.outsera.service.AwardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/award")
@Tag(name = "Awards", description = "Endpoint para calcular os intervalos de wins dos produtores")
public class AwardController {

	@Autowired
	private AwardService awardService;
	
	@GetMapping("/intervals")
	@Operation(summary = "Obtém produtores com intervalores míminos "
			+ "e máximos de premiações",
	           description = "Retorna uma lista de produtores com intervalos "
	           		+ "mais curtos e longos de premiações recebidas")
	public Map<String, List<AwardIntervalDTO>> getMinMaxInterval() {
		return awardService.getMinMaxIntervals();
	}
}
