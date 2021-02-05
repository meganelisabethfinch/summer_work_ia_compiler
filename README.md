# Summer Work Part IA/IB: Compiler

This is a handwritten lexer and SLR parser.

## Task
> You are to write a lexer and a parser for a new calculator application. For the purposes of this
exercise, you may not use lex, yacc, or other compiler compilers! You may use ML, Java or C#.
The calculator is to read in ASCII strings containing arithmetic expressions (e.g. “2.3+4”) and you
should emit a parse tree for the calculation. Accepted input numbers are to be *signed floating point*
numbers. The operators, in order of increasing precedence are:

Operator | Description
-------- | -----------
\+ | A diadic infix operator, left associative
\- | A diadic infix operator, left associative
\* | A diadic infix operator, right associative
cos | A monadic prefix operator
! | A monadic postfix operator

> 1. Define production rules for a grammar to accept signed floating point numbers. 
> 1. An *expression* is a valid use of one of the operators above. Give production rules to show
how each of the above operators can be used, noting the associativity and precedence.
> 1. List the *terminal* and *non-terminal* symbols of the language, and define an entry point for
the grammar of calculations.
> 1. Write a program to lex an input ASCII string into tokens accepted by this language. Give
some thought to the data structure you use to represent the token stream output.
> 1. Write a parser based on an LR(0) technique to convert the token stream into a parse tree. Implement the
LR(0) algorithm corresponding to the technique you used and give it the tables which define
your grammar.
> 1. Test your program, giving some (convincing) evidence. Make sure your program
correctly rejects invalid input!

## Solution
My production rules for accepting signed floating point numbers are

    float   ::= - ufloat | ufloat
    ufloat  ::= decimal | decimal e+ digits | decimal e-digits
    decimal ::= digits . digits | digits
    digits  ::= digits digit | digit
    digit   ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9

The production rules of my grammar are

    expression ::= expression + difference | difference
    difference ::= difference - product | product
    product    ::= optcos * product | optcos
    optcos     ::= cos optcos | optfact
    optfact    ::= optfact ! | statement
    statement  ::= (expression) | float
   
The lexer converts the input into a queue of tokens (terminals). The parser emits a parse tree in the form `X(Y)(Z)` where nodes `Y` and `Z` are the children of node `X`.

## Usage

    Usage: <expression>
    
For example, for the expression `2*3+4`, the token stream produced by the lexer is

    [<UFLOAT, 2.0>, <MULT>, <UFLOAT, 3.0>, <PLUS>, <UFLOAT, 4.0>]

and the parse tree emitted by the parser is

    EXPR(EXPR(DIFF(PROD(OPTCOS(OPTFACT(STATEMENT(FLOAT(UFLOAT)))))(MULT)(PROD(OPTCOS(OPTFACT(STATEMENT(FLOAT(UFLOAT)))))))))(PLUS)(DIFF(PROD(OPTCOS(OPTFACT(STATEMENT(FLOAT(UFLOAT)))))))
