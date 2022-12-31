package com.example.gt_2m_7_calculator;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static TextView text_output;
    private static TextView result;
    private static ArrayList<BigInteger> numbers;
    private static ArrayList<String> operations;
    private static Boolean isFull;
    private static Boolean isNewNumber;


    public static Integer count(String str, String target) {
        return (str.length() - str.replace(target, "").length()) / target.length();
    }


    private Boolean isOperation(){
        return (numbers.size() == operations.size());
    }

    public static Integer getLastOperationSymbolIndex(String str) {
        int lastIndex = -1;
        char[] symbols = {'+', '-', '÷', '×'};
        for (char symbol : symbols) {
            int index = str.lastIndexOf(symbol);
            if (index > lastIndex) {
                lastIndex = index;
            }
        }
        return lastIndex;
    }

    private static int getOperatorPriority(String operator) {
        if (operator.equals("×") || operator.equals("÷")) {
            return 2;
        } else if (operator.equals("+") || operator.equals("-")) {
            return 1;
        } else {
            return 0;
        }
    }


    public static String calculate(ArrayList<BigInteger> numbers, ArrayList<String> operators) {
        // Создаем стек для хранения чисел
        Stack<BigInteger> stack = new Stack<>();
        // Создаем стек для хранения операторов
        Stack<String> operatorStack = new Stack<>();

        // Инициализируем итераторы для списков чисел и операторов
        Iterator<BigInteger> numberIter = numbers.iterator();
        Iterator<String> operatorIter = operators.iterator();

        // Перебираем числа и операторы
        while (numberIter.hasNext()) {
            // Помещаем число в стек
            stack.push(numberIter.next());
            // Если есть еще операторы, то проверяем их приоритет
            if (operatorIter.hasNext()) {
                String token = operatorIter.next();
                // Пока в стеке операторов есть элементы и приоритет текущего оператора меньше или равен приоритету
                // оператора на вершине стека, берем два последних числа из стека, выполняем операцию и помещаем
                // результат обратно в стек
                while (!operatorStack.isEmpty() && getOperatorPriority(token) <= getOperatorPriority(operatorStack.peek())) {
                    BigInteger num2 = stack.pop();
                    BigInteger num1 = stack.pop();
                    String operator = operatorStack.pop();
                    if (operator.equals("+")) {
                        stack.push(num1.add(num2));
                    } else if (operator.equals("-")) {
                        stack.push(num1.subtract(num2));
                    } else if (operator.equals("×")) {
                        stack.push(num1.multiply(num2));
                    } else if (operator.equals("÷")) {
                        if(num2.intValue() == 0){
                            return "Деление на 0";
                        }
                        stack.push(num1.divide(num2));
                    }
                }

// Помещаем текущий оператор в стек операторов
            operatorStack.push(token);

            }
        }
        // После окончания цикла в стеке операторов могут остаться необработанные операторы
        while (!operatorStack.isEmpty()) {
            BigInteger num2 = stack.pop();
            BigInteger num1 = stack.pop();
            String operator = operatorStack.pop();
            if (operator.equals("+")) {
                stack.push(num1.add(num2));
            } else if (operator.equals("-")) {
                stack.push(num1.subtract(num2));
            } else if (operator.equals("×")) {
                stack.push(num1.multiply(num2));
            } else if (operator.equals("÷")) {
                if(num2.intValue() == 0){
                    return "Деление на 0";
                }
                stack.push(num1.divide(num2));
            }
        }

// Возвращаем результат из стека
        return String.valueOf(stack.pop());
    }

    public static String getAnswer(View view) {
        if (numbers.size() > 0) {
            String answer;
            if(numbers.size() == operations.size()){
                ArrayList<String> op2 = (ArrayList<String>) operations.clone();
                op2.remove(operations.size()-1);
                answer = calculate(numbers, op2);
            }else {
                answer = calculate(numbers, operations);
            }
            if(answer.equals("Деление на 0")){
                return answer;
            }
            Log.d("niko", String.valueOf(answer));
            if (answer.toString().length() > 9) {
                String str = answer.toString();
                Double ans = Double.parseDouble(str.charAt(0) + "." + str.substring(1, 7));

                return ("= " + ans + "e" + (answer.toString().length() - 1));
            } else {
                return ("= " + answer);
            }
        } else {
            result.setVisibility(View.GONE);
            return "= 0";
        }

    }

    private static String getAnswerFullText() {
        int i = 1;
        if (numbers.size() > 0) {
            String text = numbers.get(0).toString();
            if (!operations.isEmpty()) {
                text += operations.get(0);
            }
            for (int j = 1; j < numbers.size(); j++) {
                if (count(text, "\n") > 0) {
                    if (text.substring(text.lastIndexOf("\n")).length() + numbers.get(j).toString().length() > 15) {
                        text += "\n";
                        if (count(text, "\n") == 5) {
                            isFull = true;
                            break;
                        }
                    }
                } else {
                    if (numbers.get(j - 1).toString().length() + numbers.get(j).toString().length() > 15) {
                        text += "\n";
                        if (count(text, "\n") == 5) {
                            isFull = true;
                            break;
                        }
                    } else if (text.length() + numbers.get(j).toString().length() > 15) {
                        text += "\n";
                        if (count(text, "\n") == 5) {
                            isFull = true;
                            break;
                        }
                    }
                }
                text += numbers.get(j);
                if (operations.size() > i) {
                    text += operations.get(i);
                    i++;
                }

            }
            return text;
        } else {
            return "0";
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_output = findViewById(R.id.text_output);
        result = findViewById(R.id.result);
        numbers = new ArrayList<>();
        operations = new ArrayList<>();
        isFull = false;
        isNewNumber = true;


    }

    public void onNumberClick(View view) {
        result.setVisibility(View.VISIBLE);
        if (text_output.length() < 9) {
            text_output.setTextSize(60);
        } else if (text_output.length() == 9) {
            text_output.setTextSize(50);
        } else if (text_output.length() == 12) {
            text_output.setTextSize(40);
        } else if (getLastOperationSymbolIndex(text_output.getText().toString()) == -1 && text_output.getText().length() == 14) {
            return;
        }


        if (text_output.getText().toString().equals("0")) {
            text_output.setText("");
        }

        if (numbers.size() > 0 && !isNewNumber) {
            if (numbers.get(numbers.size() - 1).toString().length() == 14) {
                return;
            }
        }

        if (isFull) {
            return;
        }

        switch (view.getId()) {
            case R.id.btn_0:
                if (isNewNumber) {
                    numbers.add(BigInteger.valueOf(0));
                } else {
                    numbers.set(numbers.size() - 1, BigInteger.valueOf(Long.parseLong(numbers.get(numbers.size() - 1) + "0")));
                }
                break;
            case R.id.btn_1:
                if (isNewNumber) {
                    numbers.add(BigInteger.valueOf(1));
                } else {
                    numbers.set(numbers.size() - 1, BigInteger.valueOf(Long.parseLong(numbers.get(numbers.size() - 1) + "1")));
                }
                break;
            case R.id.btn_2:
                if (isNewNumber) {
                    numbers.add(BigInteger.valueOf(2));
                } else {
                    numbers.set(numbers.size() - 1, BigInteger.valueOf(Long.parseLong(numbers.get(numbers.size() - 1) + "2")));
                }
                break;
            case R.id.btn_3:
                if (isNewNumber) {
                    numbers.add(BigInteger.valueOf(3));
                } else {
                    numbers.set(numbers.size() - 1, BigInteger.valueOf(Long.parseLong(numbers.get(numbers.size() - 1) + "3")));
                }
                break;
            case R.id.btn_4:
                if (isNewNumber) {
                    numbers.add(BigInteger.valueOf(4));
                } else {
                    numbers.set(numbers.size() - 1, BigInteger.valueOf(Long.parseLong(numbers.get(numbers.size() - 1) + "4")));
                }
                break;
            case R.id.btn_5:
                if (isNewNumber) {
                    numbers.add(BigInteger.valueOf(5));
                } else {
                    numbers.set(numbers.size() - 1, BigInteger.valueOf(Long.parseLong(numbers.get(numbers.size() - 1) + "5")));
                }
                break;
            case R.id.btn_6:
                if (isNewNumber) {
                    numbers.add(BigInteger.valueOf(6));
                } else {
                    numbers.set(numbers.size() - 1, BigInteger.valueOf(Long.parseLong(numbers.get(numbers.size() - 1) + "6")));
                }
                break;
            case R.id.btn_7:
                if (isNewNumber) {
                    numbers.add(BigInteger.valueOf(7));
                } else {
                    numbers.set(numbers.size() - 1, BigInteger.valueOf(Long.parseLong(numbers.get(numbers.size() - 1) + "7")));
                }
                break;
            case R.id.btn_8:
                if (isNewNumber) {
                    numbers.add(BigInteger.valueOf(8));
                } else {
                    numbers.set(numbers.size() - 1, BigInteger.valueOf(Long.parseLong(numbers.get(numbers.size() - 1) + "8")));
                }
                break;
            case R.id.btn_9:
                if (isNewNumber) {
                    numbers.add(BigInteger.valueOf(9));
                } else {
                    numbers.set(numbers.size() - 1, BigInteger.valueOf(Long.parseLong(numbers.get(numbers.size() - 1) + "9")));
                }
                break;

        }
        isNewNumber = false;
        text_output.setText(getAnswerFullText());
        result.setText(getAnswer(result));
    }

    public void onClearClick(View view) {
        switch (view.getId()) {
            case R.id.clear:
                text_output.setTextSize(60);
                result.setTextSize(30);
                isNewNumber = true;
                text_output.setText("0");
                numbers.clear();
                operations.clear();
                result.setVisibility(View.GONE);
                break;
            case R.id.delete:
                if (numbers.size() == 0) {
                    isNewNumber = true;
                    text_output.setText("0");
                    result.setVisibility(View.GONE);
                    break;
                }else {
                    if (numbers.size() > operations.size()) {
                        if (numbers.get(numbers.size() - 1).toString().length() == 1) {
                            numbers.remove(numbers.size() - 1);
                            isNewNumber = true;
                        } else {
                            numbers.set(numbers.size() - 1, BigInteger.valueOf(Long.parseLong(numbers.get(numbers.size() - 1).toString().substring(0, numbers.get(numbers.size() - 1).toString().length() - 1))));
                        }
                    } else {
                        operations.remove(operations.size() - 1);
                    }
                    text_output.setText(getAnswerFullText());
                    result.setText(getAnswer(result));
                }
                isFull = false;
                break;
        }
    }

    public void addOperationCLick(View view) {
        if(numbers.size() == 0){
            numbers.add(BigInteger.valueOf(0));
        }


        switch (view.getId()) {
            case R.id.plus:
                if(isOperation()){
                    operations.set(operations.size()-1, "+");
                    text_output.setText(getAnswerFullText());
                    return;
                }
                if (isFull) {
                    return;
                }
                operations.add("+");
                text_output.setText(getAnswerFullText());
                break;
            case R.id.minus:
                if(isOperation()){
                    operations.set(operations.size()-1, "-");
                    text_output.setText(getAnswerFullText());
                    return;
                }
                if (isFull) {
                    return;
                }
                operations.add("-");
                text_output.setText(getAnswerFullText());
                break;
            case R.id.ymno:
                if(isOperation()){
                    operations.set(operations.size()-1, "×");
                    text_output.setText(getAnswerFullText());
                    return;
                }
                if (isFull) {
                    return;
                }
                operations.add("×");
                text_output.setText(getAnswerFullText());
                break;
            case R.id.delenye:
                if(isOperation()){
                    operations.set(operations.size()-1, "÷");
                    text_output.setText(getAnswerFullText());
                    return;
                }
                if (isFull) {
                    return;
                }
                operations.add("÷");
                text_output.setText(getAnswerFullText());
                break;
        }
        isNewNumber = true;
    }

    public void showResultClick(View view) {
        String answer = getAnswer(view);
        text_output.setTextSize(20);
        result.setTextSize(50);
        result.setText(answer);
    }

    public void onDotClick(View view) {
        if(isNewNumber){
            return;
        }

    }
}