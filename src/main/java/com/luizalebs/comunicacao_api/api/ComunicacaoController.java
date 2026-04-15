package com.luizalebs.comunicacao_api.api;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.business.service.ComunicacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comunicacao")
@Tag(name = "Comunicação", description = "Gerenciamento de comunicações")
public class ComunicacaoController {

    private final ComunicacaoService service;


    public ComunicacaoController(ComunicacaoService service) {
        this.service = service;
    }

    @PostMapping("/agendar")
    @Operation(summary = "Agendar comunicação",
            description = "Cria uma nova comunicação. O campo data deve estar no formato: yyyy-MM-dd HH:mm:ss")
    @ApiResponse(responseCode = "201", description = "Comunicação agendada com sucesso")
    @ApiResponse(responseCode = "409", description = "Conflito de dados")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<ComunicacaoOutDTO> agendar(@RequestBody ComunicacaoInDTO dto)  {
        return ResponseEntity.ok(service.agendarComunicacao(dto));
    }

    @GetMapping()
    @Operation(summary = "Buscar status da comunicação", description = "Retorna o status da comunicação pelo email")
    @ApiResponse(responseCode = "200", description = "Status encontrado")
    @ApiResponse(responseCode = "404", description = "Comunicação não encontrada")
    public ResponseEntity<ComunicacaoOutDTO> buscarStatus(@RequestParam String emailDestinatario) {
        return ResponseEntity.ok(service.buscarStatusComunicacao(emailDestinatario));
    }

    @PatchMapping("/cancelar")
    @Operation(summary = "Cancelar comunicação", description = "Altera o status da comunicação para cancelado")
    @ApiResponse(responseCode = "200", description = "Comunicação cancelada com sucesso")
    @ApiResponse(responseCode = "404", description = "Comunicação não encontrada")
    public ResponseEntity<ComunicacaoOutDTO> cancelarStatus(@RequestParam String emailDestinatario) {
        return ResponseEntity.ok(service.alterarStatusComunicacao(emailDestinatario));
    }
}
