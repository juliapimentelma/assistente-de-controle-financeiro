package br.com.ControleFinanceiro.API.service;

import br.com.ControleFinanceiro.API.dto.request.PossoComprarRequest;
import br.com.ControleFinanceiro.API.dto.response.IaResponse;
import br.com.ControleFinanceiro.API.entity.Usuario;
import br.com.ControleFinanceiro.API.entity.emuns.TomIA;
import br.com.ControleFinanceiro.API.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class ConselheiroIaService {

    private final ChatClient chatClient;
    private final UsuarioRepository usuarioRepository;

    public ConselheiroIaService(ChatClient.Builder chatClientBuilder, UsuarioRepository usuarioRepository) {
        this.chatClient = chatClientBuilder
                .defaultFunctions("analisarImpactoFinanceiro")
                .build();
        this.usuarioRepository = usuarioRepository;
    }

    public IaResponse analisarCompra(PossoComprarRequest request) {
        Usuario usuario = getUsuarioLogado();

        String systemPrompt = construirSystemPrompt(usuario.getTomIA(), usuario.getNome());
        String userMessage = String.format("Posso comprar um(a) '%s' que custa R$ %.2f?", request.item(), request.valor());

        String respostaLlm = chatClient.prompt()
                .system(systemPrompt)
                .user(userMessage)
                .call()
                .content();

        return new IaResponse(respostaLlm);
    }

    private String construirSystemPrompt(TomIA tomIA, String nomeUsuario) {
        String blindagem = "REGRA MÁXIMA: Você é ESTRITAMENTE o assistente financeiro do aplicativo 'Conselheiro Financeiro'. " +
                "Você NÃO DEVE responder a perguntas que não sejam sobre finanças pessoais do usuário. " +
                "Se o usuário fugir do escopo, responda: 'Foco na missão! Só falo sobre o seu dinheiro.' ";

        String basePrompt = blindagem + "Seu objetivo é analisar se o usuário " + nomeUsuario + " deve fazer a compra solicitada. " +
                "USE OBTIGATORIAMENTE A FERRAMENTA DISPONÍVEL para consultar a saúde financeira dele antes de responder. " +
                "Seja conciso (máximo 3 parágrafos). Formate valores em R$ X.XXX,XX. ";

        if (tomIA == TomIA.SARGENTO) {
            return basePrompt + "Assuma o tom de um Sargento linha dura e impiedoso. Odeie gastos desnecessários. " +
                    "Use um tom irônico e militar. Se a ferramenta indicar que a conta vai ficar negativada, dê broncas severas e " +
                    "grite com ele em letras maiúsculas. Trate-o como um recruta indisciplinado.";
        }

        return basePrompt + "Assuma o tom de um Conselheiro Financeiro amigável, acolhedor e didático. " +
                "Se a ferramenta indicar que a compra vai negativar a conta, alerte-o com gentileza sobre os riscos dos juros. " +
                "Se o saldo comportar com tranquilidade, celebre a conquista com ele.";
    }

    private Usuario getUsuarioLogado() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long usuarioId = Long.parseLong(jwt.getSubject());

        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
    }
}
