package org.dreamlang.lexer;

public enum TokenType {
  IDENT,
  NULL,
  NUMBER,
  BOOL_TRUE,
  BOOL_FALSE,
  STRING,
  CHAR,
  // 单行注释
  SINGLE_EXEGESIS,
  // 多行注释
  MULTI_EXEGESIS,
  // 关键字
  KEYWORD,
  // 赋值
  ASSIGN,
  // 加
  PLUS,
  // 减
  MINUS,
  // 乘
  MULT,
  // 除
  DIVIDE,
  // 取余
  SURPLUS,
  // 次方
  POWER,
  // 等于
  EQUAL,
  // 大于
  GREATER,
  // 小于
  LESS,
  // 大于等于
  GREATER_EQUAL,
  // 小于等于
  LESS_EQUAL,
  // 与
  LOGICAL_AND,
  // 或
  LOGICAL_OR,
  // 否
  LOGICAL_NOT,
  // 换行
  LINEBREAK,
  // 点
  DOT,
  // 逗号
  COMMA,
  // 冒号
  COLON,
  // 左小括号
  LEFT_PAREN,
  // 右小括号
  RIGHT_PAREN,
  // 左中括号
  LEFT_BRACKET,
  // 右中括号
  RIGHT_BRACKET,
  // 左大括号
  LEFT_BRACE,
  // 右大括号
  RIGHT_BRACE,
  // 文件结束
  EOF;
}
