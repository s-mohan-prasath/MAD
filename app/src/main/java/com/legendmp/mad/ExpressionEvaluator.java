package com.legendmp.mad;
import java.util.*;

public class ExpressionEvaluator {

    private int doArithmetic(int s1, int s2, char op) {
        if (op == '%') return s1 % s2;
        else if (op == '/') return s1 / s2;
        else if (op == '+') return s1 + s2;
        else if (op == '-') return s1 - s2;
        else if (op == 'x') return s1 * s2; // 'x' used instead of '*'
        else return s1;
    }

    public String evalExp(String exp) {
        if (!(validateParenthesis(exp) && validateExpression(exp))) return "Invalid Expression";
        List<String> listExp = new ArrayList<>();
        int len = exp.length();
        String number = "";
        char op;

        // Convert infix to tokens
        for (int i = 0; i < len; i++) {
            op = exp.charAt(i);
            if (op == '(' || op == ')' || isOperator(op)) {
                if (!number.isEmpty()) {
                    listExp.add(number);
                    number = "";
                }
                listExp.add(Character.toString(op));
            } else {
                number += op;
            }
        }
        if (!number.isEmpty()) listExp.add(number);

        // Handle negative numbers at the start
        if (listExp.size() > 1 && listExp.get(0).equals("-") && !listExp.get(1).equals("(")) {
            listExp.set(0, listExp.get(0) + listExp.get(1)); // Merge `-` with number
            listExp.remove(1);
        }

        Stack<String> stack = new Stack<>();
        List<String> postFix = new ArrayList<>();

        // Convert infix to postfix
        for (String operand : listExp) {
            if (operand.equals("(")) {
                stack.push(operand);
            } else if (operand.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    postFix.add(stack.pop());
                }
                if (!stack.isEmpty() && stack.peek().equals("(")) stack.pop(); // Remove "("
                else return "Invalid Expression"; // Unmatched ")"
            } else if (isOperator(operand.charAt(0))) {
                while (!stack.isEmpty() && isOperator(stack.peek().charAt(0)) &&
                        getOperatorPrecedence(stack.peek().charAt(0)) >= getOperatorPrecedence(operand.charAt(0))) {
                    postFix.add(stack.pop()); // Pop all operators of higher or equal precedence
                }
                stack.push(operand);
            } else {
                postFix.add(operand);
            }
        }

        while (!stack.isEmpty()) {
            if (stack.peek().equals("(")) return "Invalid Expression"; // Unmatched "("
            postFix.add(stack.pop());
        }

        // ✅ Postfix Expression Computed, Now Evaluate it
        return evaluatePostFix(postFix);
    }

    private String evaluatePostFix(List<String> postFix) {
        Stack<Integer> evalStack = new Stack<>();

        for (String token : postFix) {
            if (isOperator(token.charAt(0))) {
                // Pop two operands
                if (evalStack.size() < 2) return "Invalid Expression"; // Safety check

                int s2 = evalStack.pop(); // Second operand (right)
                int s1 = evalStack.pop(); // First operand (left)
                int result = doArithmetic(s1, s2, token.charAt(0));

                evalStack.push(result);
            } else {
                // Push number onto stack
                evalStack.push(Integer.parseInt(token));
            }
        }

        // Final result should be on top of stack
        return evalStack.size() == 1 ? String.valueOf(evalStack.pop()) : "Invalid Expression";
    }

    private int getOperatorPrecedence(char c) {
        if (c == 'x' || c == '/' || c == '%') return 3;
        else if (c == '+' || c == '-') return 2;
        else return 1;
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == 'x' || c == '/' || c == '%';
    }

    private boolean validateParenthesis(String exp) {
        Stack<Character> stack = new Stack<>();
        for (char c : exp.toCharArray()) {
            if (c == '(') stack.push(c);
            else if (c == ')') {
                if (!stack.isEmpty()) stack.pop();
                else return false;
            }
        }
        return stack.isEmpty();
    }

    private boolean validateExpression(String exp) {
        int len = exp.length();
        boolean isPrevOperator = (len != 0) && isOperator(exp.charAt(0));
        for (int i = 1; i < len; i++) {
            if (isOperator(exp.charAt(i)) && isPrevOperator) return false;
        }
        return !isOperator(exp.charAt(len - 1));
    }

    public static void main(String[] args) {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        String expression1 = "10+20x3";
        String expression2 = "(5+3)x2";
        String expression3 = "50/5+6x2";
        String expression4 = "10-5x2";
        String expression5 = "10x(3+2)";

        System.out.println(expression1 + " = " + evaluator.evalExp(expression1)); // Expected: 70
        System.out.println(expression2 + " = " + evaluator.evalExp(expression2)); // Expected: 16
        System.out.println(expression3 + " = " + evaluator.evalExp(expression3)); // Expected: 22
        System.out.println(expression4 + " = " + evaluator.evalExp(expression4)); // Expected: 0
        System.out.println(expression5 + " = " + evaluator.evalExp(expression5)); // Expected: 50
    }
}
