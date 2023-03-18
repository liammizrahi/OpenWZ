import WZ.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        runFile("/Users/liammizrahi/example.wz");

        if (args.length > 1) {
            System.out.println("Usage: wz [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runFile(String path) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            run(new String(bytes, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void runPrompt() {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) {
            System.out.print("> ");
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                System.err.println("Error reading input: " + e.getMessage());
                System.exit(1);
            }

            if (line == null) {
                break;
            }

            run(line);
        }
    }

    private static void run(String source) {
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Interpreter interpreter = new Interpreter();

        try {
            List<ASTNode> program = parser.parse();
            for (ASTNode node : program) {
                interpreter.interpret(node);
            }
        } catch (RuntimeError error) {
            System.err.println("Runtime error: " + error.getMessage());
        }
    }
}