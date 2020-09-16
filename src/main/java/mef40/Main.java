package mef40;

import mef40.lexer.Lexer;
import mef40.parser.Production;

import java.io.EOFException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	// write your code here
        var lexer = new Lexer();

        String s1 = "72.3 + 45.3";



        try {
            lexer.lex(s1);
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
