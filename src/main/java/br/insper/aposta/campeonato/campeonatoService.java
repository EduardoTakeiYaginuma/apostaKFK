package br.insper.aposta.campeonato;

import br.insper.aposta.aposta.Aposta;
import br.insper.aposta.aposta.ApostaRepository;
import br.insper.loja.partida.dto.RetornarPartidaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class campeonatoService {

    @Autowired
    private ApostaRepository apostaRepository;

    @KafkaListener(topics = "partidas")
    public void getPartidas(RetornarPartidaDTO dto){
        Integer placarMandante = dto.getPlacarMandante();
        Integer placarVisitante = dto.getPlacarVisitante();
        List<Aposta> apostas = apostaRepository.findAll();
        for (Aposta aposta : apostas){
            if (aposta.getIdPartida().equals(dto.getId())){
                if (placarMandante > placarVisitante){
                    if (aposta.getResultado().equals("Mandante")){
                        aposta.setStatus("Ganhou");
                    }
                } else if (placarMandante < placarVisitante){
                    if (aposta.getResultado().equals("Visitante")){
                        aposta.setStatus("Ganhou");
                    }
                } else {
                    if (aposta.getResultado().equals("Empate")){
                        aposta.setStatus("Ganhou");
                    }
                }
                apostaRepository.save(aposta);
            }
        }

    }


}
