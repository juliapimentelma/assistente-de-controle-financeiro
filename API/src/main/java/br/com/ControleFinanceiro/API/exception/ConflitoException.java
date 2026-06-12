package br.com.ControleFinanceiro.API.exception;

public class ConflitoException extends RuntimeException {
    public ConflitoException(String message) {
        super(message);
    }
}
