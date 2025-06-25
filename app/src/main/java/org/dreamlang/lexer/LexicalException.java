package org.dreamlang.lexer;

public class LexicalException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  private final String errorType;
  private final char errorChar;
  private final String errorTokenType;
  private final int errorLine;

  public LexicalException(
      final String errorType,
      final char errorChar,
      final String errorTokenType,
      final int errorLine) {
    this.errorType = errorType;
    this.errorChar = errorChar;
    this.errorTokenType = errorTokenType;
    this.errorLine = errorLine;
  }

  @Override
  public String getMessage() {
    return String.format(
        "%s from token '%c' with type %s in line %d",
        this.errorType, this.errorChar, this.errorTokenType, this.errorLine);
  }
}
