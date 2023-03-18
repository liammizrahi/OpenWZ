package interpreter;

import lexer.Lexer;
import lexer.Token;
import parser.AST.*;
import parser.*;

import java.util.List;

public class Interpreter {
    private final Environment globalEnv = new Environment();

    public Interpreter() {
        globalEnv.set("null", null);
        globalEnv.set("true", true);
        globalEnv.set("false", false);
    }

    public Object interpret(AST program) {
        return program.execute(globalEnv);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java interpreter.Interpreter <filename>");
            return;
        }

        String fileName = args[0];
        String input = Utils.readFile(fileName);
        if (input == null) {
            System.out.println("Failed to read file: " + fileName);
            return;
        }


        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();
        Parser parser = new Parser(tokens);
        AST program = parser.parseProgram();

        Interpreter interpreter = new Interpreter();
        Object result = interpreter.interpret(program);
        System.out.println(result);
    }
}
