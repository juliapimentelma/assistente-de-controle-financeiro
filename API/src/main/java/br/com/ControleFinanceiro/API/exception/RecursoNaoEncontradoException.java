package br.com.ControleFinanceiro.API.exception;

public class RecursoNaoEncontradoException extends RuntimeException {
  public RecursoNaoEncontradoException(String message) {
    super(message);
  }
}
