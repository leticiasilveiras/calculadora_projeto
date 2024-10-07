package calculadora;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class CalculadoraApp extends Application {

    private TextField display = new TextField();
    private TextArea resumoArea = new TextArea();
    private Operation currentOperation;
    private double currentResult = 0;
    private boolean startNewOperation = true;
    
    private List<Double> receitas = new ArrayList<>();
    private List<Double> despesas = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "C", "%"
        };

        int row = 1;
        int col = 0;

        for (String text : buttons) {
            Button button = new Button(text);
            button.setMinSize(50, 50);
            button.getStyleClass().add("calc-button");
            button.setOnAction(e -> processButton(text));
            grid.add(button, col, row);

            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }

        display.setEditable(false);
        display.setMinSize(210, 50);
        display.getStyleClass().add("display");
        grid.add(display, 0, 0, 4, 1);

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Button receitaButton = new Button("Receita");
        receitaButton.setMinSize(100, 50);
        receitaButton.getStyleClass().add("func-button");
        receitaButton.setOnAction(e -> addReceita());

        Button despesaButton = new Button("Despesa");
        despesaButton.setMinSize(100, 50);
        despesaButton.getStyleClass().add("func-button");
        despesaButton.setOnAction(e -> addDespesa());

        Button resumoButton = new Button("Resumo");
        resumoButton.setMinSize(100, 50);
        resumoButton.getStyleClass().add("func-button");
        resumoButton.setOnAction(e -> showResumo());

        resumoArea.setEditable(false);
        resumoArea.setPrefHeight(150);
        resumoArea.setPrefWidth(250);
        resumoArea.getStyleClass().add("resumo-area");

        vbox.getChildren().addAll(grid, receitaButton, despesaButton, resumoButton, resumoArea);

        Scene scene = new Scene(vbox, 300, 500);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Calculadora Financeira");
        primaryStage.show();
    }

    private void processButton(String text) {
        switch (text) {
            case "=":
                if (currentOperation != null) {
                    calculateResult();
                    currentOperation = null;
                }
                break;
            case "C":
                display.clear();
                currentResult = 0;
                currentOperation = null;
                startNewOperation = true;
                break;
            case "+":
                setOperation(new Addition());
                break;
            case "-":
                setOperation(new Subtraction());
                break;
            case "*":
                setOperation(new Multiplication());
                break;
            case "/":
                setOperation(new Division());
                break;
            case "%":
                break;
            default:
                if (startNewOperation) {
                    display.clear();
                    startNewOperation = false;
                }
                display.appendText(text);
                break;
        }
    }

    private void setOperation(Operation operation) {
        if (currentOperation != null) {
            calculateResult();
        }
        currentOperation = operation;
        currentResult = Double.parseDouble(display.getText());
        startNewOperation = true;
    }

    private void calculateResult() {
        double secondOperand = Double.parseDouble(display.getText());
        if (currentOperation != null) {
            currentResult = currentOperation.execute(currentResult, secondOperand);
            display.setText(String.valueOf(currentResult));
        } else {
            display.setText("Error");
        }
    }

    private void addReceita() {
        try {
            double valor = Double.parseDouble(display.getText());
            receitas.add(valor);
            display.clear();
            display.setText("Receita Adicionada");
            startNewOperation = true;
        } catch (NumberFormatException e) {
            display.setText("Erro: Entrada Inválida");
        }
    }

    private void addDespesa() {
        try {
            double valor = Double.parseDouble(display.getText());
            despesas.add(valor);
            display.clear();
            display.setText("Despesa Adicionada");
            startNewOperation = true;
        } catch (NumberFormatException e) {
            display.setText("Erro: Entrada Inválida");
        }
    }

    private void showResumo() {
        double totalReceitas = receitas.stream().mapToDouble(Double::doubleValue).sum();
        double totalDespesas = despesas.stream().mapToDouble(Double::doubleValue).sum();
        double saldo = totalReceitas - totalDespesas;

        resumoArea.clear();
        resumoArea.setText("Receitas: " + totalReceitas + "\nDespesas: " + totalDespesas + "\nSaldo: " + saldo);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

