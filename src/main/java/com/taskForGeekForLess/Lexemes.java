package com.taskForGeekForLess;

import java.util.List;

public class Lexemes {

    private int position;
    private final List<Lexeme> lexemes;

    public Lexemes(List<Lexeme> lexemes) {
        this.position = 0;
        this.lexemes = lexemes;
    }

    public Lexeme nextLexeme(){return this.lexemes.get(position++);}

    public Lexeme previousLexeme(){return this.lexemes.get(--position);}

    public boolean reducePosition(){
        position--;
        if (position < 0){
            position++;
            return false;
        }else {
            return true;
        }
    }

    public boolean increasePosition(){
        position++;
        if (position >= lexemes.size()){
            position--;
            return false;
        }else {
            return true;
        }
    }

    public void setPosition(int position) {this.position = position;}
    public int getPosition() {return position;}

    @Override
    public String toString() {
        return "Lexemes{" +
                "lexemes=" + lexemes +
                '}';
    }
}
