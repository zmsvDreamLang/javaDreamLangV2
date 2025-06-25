package org.dreamlang.lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexical {
  // Constants to avoid literals in if conditions
  private static final char QUOTE_CHAR = '"';
  private static final char SINGLE_QUOTE_CHAR = '\'';
  private static final char EQUAL_CHAR = '=';
  private static final char AMPERSAND_CHAR = '&';
  private static final char PIPE_CHAR = '|';
  private static final char BACKSLASH_CHAR = '\\';

  // String constants to avoid literals in comparisons
  private static final String NULL_LITERAL = "null";
  private static final String TRUE_LITERAL = "true";
  private static final String FALSE_LITERAL = "false";

  private static final Pattern PATTERN = Pattern.compile("\\\\([\"bfnrt])");
  private static final boolean[] NUMBERS = new boolean[65_535];
  private static final boolean[] SIX_NUMBERS = new boolean[65_535];
  private static final boolean[] SPECIAL_CHARS = new boolean[65_535];
  private static final Set<String> KEYWORDS = new HashSet<>();
  private static final StringBuilder REPLACE_SB = new StringBuilder();

  private final String data;
  private final StringBuilder stringBuilder;
  private int index;
  private int line;

  static {
    for (int i = '0'; i <= '9'; i++) {
      NUMBERS[i] = true;
      SIX_NUMBERS[i] = true;
    }

    for (int i = 'a'; i <= 'f'; i++) {
      SIX_NUMBERS[i] = true;
    }

    for (int i = 'A'; i <= 'F'; i++) {
      SIX_NUMBERS[i] = true;
    }

    final char[] chars = {
      ' ', '\n', '\r', '\t', '.', ',', '(', ')', '{', '}', '[', ']', '+', '-', '*', '/', ';', '<',
      '>', '=', '|', '&'
    };
    for (final char c : chars) {
      SPECIAL_CHARS[c] = true;
    }

    final String[] strings = {
      "bool",
      "number",
      "char",
      "string",
      "function",
      "array",
      "class",
      "object",
      "reference",
      "package",
      "import",
      "var",
      "val",
      "ref",
      "return",
      "fun",
      "if",
      "else",
      "for",
      "while",
      "break",
      "continue",
      "switch",
      "case",
      "default",
      "super",
      "this",
      "available",
      "in",
      "interface",
      "abstract"
    };
    KEYWORDS.addAll(Arrays.asList(strings));
  }

  private static String processString(final String text) {
    final Matcher matcher = PATTERN.matcher(text);

    REPLACE_SB.setLength(0);

    while (matcher.find()) {
      final String match = matcher.group();
      switch (match) {
        case "\\\"":
          matcher.appendReplacement(REPLACE_SB, "\"");
          break;
        case "\\b":
          matcher.appendReplacement(REPLACE_SB, "\b");
          break;
        case "\\f":
          matcher.appendReplacement(REPLACE_SB, "\f");
          break;
        case "\\n":
          matcher.appendReplacement(REPLACE_SB, "\n");
          break;
        case "\\r":
          matcher.appendReplacement(REPLACE_SB, "\r");
          break;
        case "\\t":
          matcher.appendReplacement(REPLACE_SB, "\t");
          break;
        default:
          // No action needed for other cases
          break;
      }
    }
    matcher.appendTail(REPLACE_SB);
    final String processedText = REPLACE_SB.toString().replaceAll("\\\\\\\\", "\\\\");
    return processedText.substring(1, processedText.length() - 1);
  }

  public Lexical(final String data) {
    this.data = data;
    this.stringBuilder = new StringBuilder();
    this.index = 0;
    this.line = 1;
  }

  private Token nextToken() {
    stringBuilder.setLength(0);
    Token token = null;
    /* if(index > data.length()-1){
        token = new Token(TokenType.EOF,line,"EOF");
        return token;
      }
    */
    final char firstChar = data.charAt(index);
    final int start = index;
    index++;
    switch (firstChar) {
      case ' ':
      case '\t':
      case ';':
        break;
      case '\n':
      case '\r':
        line++;
        break;
      case '.':
        token = new Token(TokenType.DOT, line, ".");
        break;
      case ',':
        token = new Token(TokenType.COMMA, line, ",");
        break;
      case ':':
        token = new Token(TokenType.COLON, line, ":");
        break;
      case '(':
        token = new Token(TokenType.LEFT_PAREN, line, "(");
        break;
      case ')':
        token = new Token(TokenType.RIGHT_PAREN, line, ")");
        break;
      case '[':
        token = new Token(TokenType.LEFT_BRACKET, line, "[");
        break;
      case ']':
        token = new Token(TokenType.RIGHT_BRACKET, line, "]");
        break;
      case '{':
        token = new Token(TokenType.LEFT_BRACE, line, "{");
        break;
      case '}':
        token = new Token(TokenType.RIGHT_BRACE, line, "}");
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
        stringBuilder.append(firstChar);
        boolean isSix = false;
        boolean isDec = false;
        while (index <= data.length() - 1) {
          final char currentChar = data.charAt(index);
          if (currentChar == 'x' && index == start + 1) {
            stringBuilder.append(currentChar);
            isSix = true;
          } else if (NUMBERS[currentChar]) {
            stringBuilder.append(currentChar);
          } else if (isSix && SIX_NUMBERS[currentChar]) {
            stringBuilder.append(currentChar);
          } else if (currentChar == '.' && !isDec) {
            isDec = true;
            stringBuilder.append(currentChar);
          } else {
            if (currentChar == '.'
                || !SPECIAL_CHARS[currentChar]
                || stringBuilder.charAt(stringBuilder.length() - 1) == '.') {
              throw new LexicalException("Illegal Character", currentChar, "NUMBER", line);
            }
            break;
          }
          index++;
        }
        token = new Token(TokenType.NUMBER, line, stringBuilder.toString());
        break;
      case QUOTE_CHAR:
      case SINGLE_QUOTE_CHAR:
        final char closeChar = firstChar;
        while (index <= data.length() - 1) {
          final char currentChar = data.charAt(index);
          if (currentChar == BACKSLASH_CHAR) {
            index += 2;
          } else if (currentChar == closeChar) {
            index++;
            break;
          } else {
            index++;
          }
        }
        if (closeChar == SINGLE_QUOTE_CHAR) {
          token = new Token(TokenType.CHAR, line, processString(data.substring(start, index)));
        } else {
          token = new Token(TokenType.STRING, line, processString(data.substring(start, index)));
        }
        break;
      case '+':
        token = new Token(TokenType.PLUS, line, "+");
        break;
      case '-':
        token = new Token(TokenType.MINUS, line, "-");
        break;
      case '*':
        token = new Token(TokenType.MULT, line, "*");
        break;
      case '/':
        token = new Token(TokenType.DIVIDE, line, "/");
        break;
      case '%':
        token = new Token(TokenType.SURPLUS, line, "%");
        break;
      case '^':
        token = new Token(TokenType.POWER, line, "^");
        break;
      case '=':
        stringBuilder.append(firstChar);
        if (index <= data.length() - 1 && data.charAt(index) == EQUAL_CHAR) {
          index++;
          stringBuilder.append(EQUAL_CHAR);
          token = new Token(TokenType.EQUAL, line, stringBuilder.toString());
        } else {
          token = new Token(TokenType.ASSIGN, line, stringBuilder.toString());
        }
        break;
      case '>':
      case '<':
        stringBuilder.append(firstChar);
        if (index <= data.length() - 1 && data.charAt(index) == EQUAL_CHAR) {
          index++;
          stringBuilder.append(EQUAL_CHAR);
          if (firstChar == '>') {
            token = new Token(TokenType.GREATER_EQUAL, line, stringBuilder.toString());
          } else {
            token = new Token(TokenType.LESS_EQUAL, line, stringBuilder.toString());
          }
        } else {
          if (firstChar == '>') {
            token = new Token(TokenType.GREATER, line, stringBuilder.toString());
          } else {
            token = new Token(TokenType.LESS, line, stringBuilder.toString());
          }
        }
        break;
      case '&':
      case '|':
        if (index <= data.length() - 1 && data.charAt(index) == firstChar) {
          stringBuilder.append(firstChar).append(firstChar);
          index++;
          if (firstChar == AMPERSAND_CHAR) {
            token = new Token(TokenType.LOGICAL_AND, line, stringBuilder.toString());
          } else if (firstChar == PIPE_CHAR) {
            token = new Token(TokenType.LOGICAL_OR, line, stringBuilder.toString());
          }
          break;
        }
        throw new LexicalException("Syntax Error", firstChar, "SPECICAL_OPERATOR", line);
      case '!':
        stringBuilder.append(firstChar);
        token = new Token(TokenType.LOGICAL_NOT, line, stringBuilder.toString());
        break;
      default:
        stringBuilder.append(firstChar);
        while (index <= data.length() - 1) {
          final char currentChar = data.charAt(index);
          if (!SPECIAL_CHARS[currentChar]) {
            stringBuilder.append(currentChar);
            index++;
          } else {
            break;
          }
        }
        final String str = stringBuilder.toString();
        if (NULL_LITERAL.equals(str)) {
          token = new Token(TokenType.NULL, line, str);
        } else if (TRUE_LITERAL.equals(str)) {
          token = new Token(TokenType.BOOL_TRUE, line, str);
        } else if (FALSE_LITERAL.equals(str)) {
          token = new Token(TokenType.BOOL_FALSE, line, str);
        } else if (KEYWORDS.contains(str)) {
          token = new Token(TokenType.KEYWORD, line, str);
        } else {
          token = new Token(TokenType.IDENT, line, str);
        }
    }
    return token;
  }

  public List<Token> execute() {
    final List<Token> tokens = new ArrayList<>();
    while (index <= data.length() - 1) {
      final Token token = nextToken();
      if (token != null) {
        tokens.add(token);
      }
    }
    tokens.add(new Token(TokenType.EOF, line, "EOF"));
    return tokens;
  }
}
