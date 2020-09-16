package mef40.parser;

import mef40.Token;

import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public interface Action {
    void execute(Stack<Set<Item>> stack, Queue<Token> token);
}
