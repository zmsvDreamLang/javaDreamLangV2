package org.dreamlang.lexer;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexical{
  String data;
  
  private StringBuilder sb;
  private int index;
  private int line;
  
  private static boolean[] numbers = new boolean[256];
  private static boolean[] six_numbers = new boolean[256];
  private static boolean[] special_chars = new boolean[256];
  private static Set<String> keywords = new HashSet<String>();
  
  static{
    for(int i = '0';i <= '9';i++){
      numbers[i] = true;
      six_numbers[i] = true;
    }
    
    for(int i = 'a';i <= 'f';i++){
      six_numbers[i] = true;
    }
    
    for(int i = 'A';i <= 'F';i++){
      six_numbers[i] = true;
    }
    
    char[] chars = {' ','\n','\r','\t','.',',','(',')','{','}','+','-','*','/',';','<','>','=','|','&'};
    for(char c : chars){
      special_chars[(int)c] = true;
    }
  }
  
  private static String replaceSpecialChars(String text){
    Pattern pattern = Pattern.compile("\\\\\"|\\\\b|\\\\f|\\\\n|\\\\r|\\\\t");
    Matcher matcher = pattern.matcher(text);
    
    StringBuilder sb = new StringBuilder();
    int lastEnd = 0;
    
    while(matcher.find()){
      String match = matcher.group();
      switch(match){
        case "\\\"":
          sb.append(text,lastEnd,matcher.start()).append("\"");
          break;
        case "\\b":
          sb.append(text,lastEnd,matcher.start()).append("\b");
          break;
        case "\\f":
          sb.append(text,lastEnd,matcher.start()).append("\f");
          break;
        case "\\n":
          sb.append(text,lastEnd,matcher.start()).append("\n");
          break;
        case "\\r":
          sb.append(text,lastEnd,matcher.start()).append("\r");
          break;
        case "\\t":
          sb.append(text,lastEnd,matcher.start()).append("\t");
      }
      lastEnd = matcher.start();
    }
    matcher.appendTail(sb);
    text = sb.toString().replaceAll("\\\\\\\\","\\\\");
    return text;
  }
  
  public Lexical(String data){
    this.data = data;
    this.sb = new StringBuilder();
    this.index = 0;
    this.line = 1;
  }
  
  private Token nextToken(){
    sb.setLength(0);
    Token token = null;
    if(index > data.length()-1){
      token = new Token(TokenType.EOF,line,"EOF");
      return token;
    }
    char c = data.charAt(index);
    int start = index;
    index++;
    switch(c){
      case ' ':
      case '\t':
        break;
      case '\n':
      case '\r':
        line++;
        break;
      case '.':
        token = new Token(TokenType.DOT,line,".");
        break;
      case ',':
        token = new Token(TokenType.COMMA,line,",");
      case ':':
        token = new Token(TokenType.COLON,line,":");
        break;
      case ';':
        token = new Token(TokenType.DIVIDE,line,";");
        break;
      case '(':
        token = new Token(TokenType.LEFT_BRACKET,line,"(");
        break;
      case ')':
        token = new Token(TokenType.RIGHT_BRACKET,line,")");
        break;
      case '[':
        token = new Token(TokenType.LEFT_MPARENTH,line,"[");
        break;
      case ']':
        token = new Token(TokenType.RIGHT_MPARENTH,line,"[");
        break;
      case '{':
        token = new Token(TokenType.LEFT_BRACES,line,"{");
        break;
      case '}':
        token = new Token(TokenType.RIGHT_BRACES,line,"}");
        break;
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
        sb.append(c);
        boolean isSix = false;
        boolean isDec = false;
        while(index <= data.length()-1){
          c = data.charAt(index);
          if(c == 'x' && index == start+1){
            sb.append(c);
            isSix = true;
          }else if(numbers[c]){
            sb.append(c);
          }else if(isSix && six_numbers[c]){
            sb.append(c);
          }else if(c == '.' && !isDec){
            isDec = true;
            sb.append(c);
          }else{
            if(c == '.' || !special_chars[c] || sb.charAt(sb.length()-1) == '.'){
              throw new LexicalException("Illegal Character",c,"NUMBER",line);
            }
            break;
          }
          index++;
        }
        token = new Token(TokenType.NUMBER,line,sb.toString());
        break;
      case '\"':
      case '\'':
        char closeChar = c;
        while(index <= data.length()-1){
          c = data.charAt(index);
          if(c == '\\'){
            index += 2;
          }else if(c == closeChar){
            index++;
            break;
          }else{
            index++;
          }
        }
        token = new Token(TokenType.STRING,line,replaceSpecialChars(data.substring(start,index)));
        break;
      case '+':
      case '-':
        if(index <= data.length()-1 && data.charAt(index) == c){
          sb.append(c);
          sb.append(c);
          index++;
          token = new Token(TokenType.ADVANCE_OPERATOR,line,sb.toString());
          break;
        }
      case '*':
      case '/':
      case '%':
      case '^':
        sb.append(c);
        if(index <= data.length()-1 && data.charAt(index) == '='){
          index++;
          sb.append('=');
          token = new Token(TokenType.ASSIGN_OPERATOR,line,sb.toString());
        }else{
          token = new Token(TokenType.BASIC_OPERATOR,line,sb.toString());
        }
        break;
      case '=':
        sb.append(c);
        if(index <= data.length()-1 && data.charAt(index) == '='){
          index++;
          sb.append('=');
          token = new Token(TokenType.LOGICAL_OPERATOR,line,sb.toString())
        }else{
          token = new Token(TokenType.ASSIGN_OPERATOR,line,sb.toString());
        }
      case '>':
      case '<':
        if(index <= data.length()-1 && data.charAt(index) == '='){
          index++;
          sb.append('=');
        }
        token = new Token(TokenType.LOGICAL_OPERATOR,line,sb.toString());
        break;
      case '&':
      case '|':
        if(index <= data.length()-1 && data.charAt(index) == c){
          sb.append(c);
          sb.append(c);
          index++;
          token = new Token(TokenType.SPECICAL_OPERATOR,line,sb.toString());
          break;
        }
        throw new LexicalException("Syntax Error",c,"SPECICAL_OPERATOR",line);
      case '!':
        sb.append(c);
        token = new Token(TokenType.SPECICAL_OPERATOR,line,sb.toString());
        break;
      default:
        sb.append(c);
        while(index <= data.length()-1){
          c = data.charAt(index);
          if(!special_chars[c]){
            sb.append(c);
          }else{
            break;
          }
        }
        // 关键词未写完，故没有后续
    }
    return token;
  }
}