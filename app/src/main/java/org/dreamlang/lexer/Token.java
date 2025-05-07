package org.dreamlang.lexer;

public class Token{
  TokenType type;
  int line;
  String value;
  
  public Token(TokenType type,int line,String value){
    this.type = type;
    this.line = line;
    this.value = value;
  }
}