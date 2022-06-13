package com.taskForGeekForLess;

public class Lexeme {

    private LexemeType lexemeType;
    private String lexemeValue;

    public Lexeme(LexemeType lexemeType, String lexemeValue) {
        this.lexemeType = lexemeType;
        this.lexemeValue = lexemeValue;
    }

    public Lexeme(LexemeType lexemeType, Character lexemeValue) {
        this.lexemeType = lexemeType;
        this.lexemeValue = lexemeValue.toString();
    }

    public LexemeType getLexemeType() {return lexemeType;}
    public void setLexemeType(LexemeType lexemeType) {this.lexemeType = lexemeType;}

    public String getLexemeValue() {return lexemeValue;}
    public void setLexemeValue(String lexeme) {this.lexemeValue = lexeme;}

    @Override
    public String toString() {
        return "Lexeme{" +
                "lexemeType=" + lexemeType +
                ", lexeme='" + lexemeValue + '\'' +
                '}';
    }
}
