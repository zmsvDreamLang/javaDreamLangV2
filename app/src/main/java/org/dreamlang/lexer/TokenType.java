package org.dreamlang.lexer;

public enum TokenType{
  IDENT,
  NUMBER,
  STRING,
  BOOL,
  NULL,
  // 关键字
  KEYWORD,
  // 赋值运算符
  ASSIGN_OPERATOR,
  // 基本运算符
  BASIC_OPERATOR,
  // 逻辑运算符
  LOGICAL_OPERATOR,
  // 高级运算符
  ADVANCE_OPERATOR,
  // 特殊运算符
  SPECICAL_OPERATOR,
  // 换行
  LINEBREAK,
  // 点
  DOT,
  // 逗号
  COMMA,
  // 冒号
  COLON,
  // 分号
  DIVIDE,
  // 左小括号
  LEFT_BRACKET,
  // 右小括号
  RIGHT_BRACKET,
  // 左中括号
  LEFT_MPARENTH,
  // 右中括号
  RIGHT_MPARENTH,
  // 左大括号
  LEFT_BRACES,
  // 右大括号
  RIGHT_BRACES,
  // 文件结束
  EOF;
}