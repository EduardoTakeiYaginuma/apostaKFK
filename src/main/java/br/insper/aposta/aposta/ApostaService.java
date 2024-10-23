package br.insper.aposta.aposta;

import br.insper.loja.partida.dto.RetornarPartidaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApostaService {

    @Autowired
    private ApostaRepository apostaRepository;

    public void salvarAposta(Aposta aposta) {
        aposta.setId(UUID.randomUUID().toString());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RetornarPartidaDTO> partida = restTemplate.getForEntity(
                "http://localhost:8081/partida/" + aposta.getIdPartida(),
                RetornarPartidaDTO.class);

        if (partida.getStatusCode().is2xxSuccessful())  {
            apostaRepository.save(aposta);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi possível encontrar a partida relacionada a aposta!");
        }

    }

    public Aposta resultadoAposta(String id) {
        Optional<Aposta> aposta = apostaRepository.findById(String.valueOf(id));
        if (aposta.isPresent()) {
            return aposta.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aposta não encontrada!");
    }

    public List<Aposta> listarApostas() {
        return apostaRepository.findAll();
    }

}
