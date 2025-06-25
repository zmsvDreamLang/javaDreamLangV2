package org.dreamlang.lexer;

public class Token {
  TokenType type;
  int line;
  String value;

  public Token(final TokenType type, final int line, final String value) {
    this.type = type;
    this.line = line;
    this.value = value;
  }

  @Override
  public String toString() {
    final StringBuilder stringBuilder = new StringBuilder();
    final String[] strings = {
      "{type=\'",
      type.toString(),
      "\',",
      "line=",
      String.valueOf(line),
      ",",
      "value=\'",
      value,
      "\'}"
    };
    for (final String str : strings) {
      stringBuilder.append(str);
    }
    return stringBuilder.toString();
  }
}
