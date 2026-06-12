package br.com.ControleFinanceiro.API.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    public void bloquear(String token) {
        blacklist.add(token);
    }

    public boolean isBloqueado(String token) {
        return blacklist.contains(token);
    }
}