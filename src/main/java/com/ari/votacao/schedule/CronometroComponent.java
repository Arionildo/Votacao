package com.ari.votacao.schedule;

import com.ari.votacao.dto.SessaoVotacaoDTO;
import com.ari.votacao.enums.SituacaoEnum;
import com.ari.votacao.service.SessaoVotacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class CronometroComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(CronometroComponent.class);

    private final SessaoVotacaoService sessaoVotacaoService;

    public CronometroComponent(SessaoVotacaoService sessaoVotacaoService) {
        this.sessaoVotacaoService = sessaoVotacaoService;
    }

    @Scheduled(cron = "15 * * * * *")
    private void atualizarSessoes() {
        LOGGER.debug("Contador de tempo sendo excutado...");
        List<SessaoVotacaoDTO> list = sessaoVotacaoService.buscarSessoesParaDesativar();
        LOGGER.debug("Quantidade de sessoes abertas  = {}", list.size());
        list.forEach(dto -> {
            LOGGER.debug("Sessao encerrada {}", dto.getId());
            if (SituacaoEnum.ATIVA.equals(dto.getSituacao())) {
                sessaoVotacaoService.encerrarSessaoVotacao(dto);
            }
        });
    }
}