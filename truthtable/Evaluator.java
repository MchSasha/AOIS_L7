package truthtable;

import java.util.ArrayDeque;
import java.util.Deque;

public class Evaluator {

    private static boolean isOperand(char character) {
        return character == 't' || character == 'f';
    }

    private static boolean isOperator(char character) {
        return character == '!' || character == '*' || character == '+';
    }

    private static boolean applyOperator(char operator, boolean aValue, boolean bValue) {
        return switch (operator) {
            case '!' -> !aValue;
            case '*' -> aValue && bValue;
            case '+' -> aValue || bValue;
            default -> false;
        };
    }

    public static boolean evaluate(String formula) {
        Deque<Boolean> values = new ArrayDeque<>();
        Deque<Character> operators = new ArrayDeque<>();

        for (int iter = 0; iter < formula.length(); iter++) {
            char character = formula.charAt(iter);

            if (character == ' ') {
                continue;
            }
            if (isOperand(character)) {
                processOperand(character, values);
            } else if (isOperator(character)) {
                processOperator(character, values, operators);
            } else if (character == '(') {
                operators.push(character);
            } else if (character == ')') {
                processParentheses(values, operators);
            }
        }
        processOther(values, operators);

        return values.pop();
    }

    private static void processOperand(char character, Deque<Boolean> values) {
        if (character == 't') {
            values.push(true);
        } else if (character == 'f') {
            values.push(false);
        }
    }

    private static void processOperator(char character, Deque<Boolean> values, Deque<Character> operators) {
        while (!operators.isEmpty() && hasPrecedence(operators.peek(), character)) {
            char operator = operators.pop();
            if (operator == '!') {
                processNegation(values);
            } else {
                handleBinaryOperator(values, operator);
            }
        }
        operators.push(character);
    }

    private static void processNegation(Deque<Boolean> values) {
        boolean aValue = values.pop();
        values.push(applyOperator('!', aValue, false));
    }

    private static void handleBinaryOperator(Deque<Boolean> values, char operator) {
        boolean bValue = values.pop();
        boolean aValue = values.pop();
        values.push(applyOperator(operator, aValue, bValue));
    }

    private static void processParentheses(Deque<Boolean> values, Deque<Character> operators) {
        while (!operators.isEmpty() && operators.peek() != '(') {
            char operator = operators.pop();
            if (operator == '!') {
                processNegation(values);
            } else {
                handleBinaryOperator(values, operator);
            }
        }
        operators.pop();
    }

    private static void processOther(Deque<Boolean> values, Deque<Character> operators) {
        while (!operators.isEmpty()) {
            char operator = operators.pop();
            if (operator == '!') {
                processNegation(values);
            } else {
                handleBinaryOperator(values, operator);
            }
        }
    }

    private static boolean hasPrecedence(char operator1, char operator2) {
        if (operator2 == '(' || operator2 == ')') {
            return false;
        }
        if (operator1 == '!') {
            return true;
        }
        if ((operator1 == '*' || operator1 == '+') && operator2 == '!') {
            return true;
        }
        if (operator1 == operator2) {
            return true;
        }
        return (operator1 == '*' || operator1 == '+') && (operator2 == '*' || operator2 == '+');
    }
}