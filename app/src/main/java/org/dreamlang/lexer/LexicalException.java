package org.dreamlang.lexer;

public class LexicalException extends RuntimeException{
  private String error_type;
  private char error_char;
  private String error_token_type;
  private int error_line;
  
  public LexicalException(String error_type,char error_char,String error_token_type,int error_line){
    this.error_type = error_type;
    this.error_char = error_char;
    this.error_token_type = error_token_type;
    this.error_line = error_line;
  }
  
  @Override
  public String getMessage(){
    return String.format("%s from token '%c' with type %s in line %d",this.error_type,this.error_char,this.error_token_type,this.error_line);
  }
}