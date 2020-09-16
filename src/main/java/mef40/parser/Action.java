package mef40.parser;

import com.google.common.collect.ImmutableSet;
import mef40.Token;

import java.util.Queue;
import java.util.Stack;

public interface Action {
    void execute(Stack<ImmutableSet<Item>> states, Queue<Token> token);
}
