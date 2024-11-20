package com.example.kalkulator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Kalkulator VAT netto-brutto");

        // Główne kontenery
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));

        // Sekcja wyboru metody obliczeń
        Label methodLabel = new Label("Metoda obliczeń:");
        ToggleGroup methodGroup = new ToggleGroup();

        RadioButton nettoToBrutto = new RadioButton("Od netto do brutto");
        nettoToBrutto.setToggleGroup(methodGroup);
        nettoToBrutto.setSelected(true);

        RadioButton bruttoToNetto = new RadioButton("Od brutto do netto");
        bruttoToNetto.setToggleGroup(methodGroup);

        RadioButton adjustVAT = new RadioButton("Dopasuj do kwoty VAT");
        adjustVAT.setToggleGroup(methodGroup);

        VBox methodBox = new VBox(5, methodLabel, nettoToBrutto, bruttoToNetto, adjustVAT);

        // Sekcja danych wejściowych
        Label baseValueLabel = new Label("Wartość bazowa:");
        TextField baseValueField = new TextField();
        baseValueField.setPromptText("Podaj wartość...");

        Label vatRateLabel = new Label("Stawka VAT:");
        ComboBox<String> vatRateBox = new ComboBox<>();
        vatRateBox.getItems().addAll("23%", "8%", "5%", "0%");
        vatRateBox.setValue("23%");

        VBox inputBox = new VBox(5, baseValueLabel, baseValueField, vatRateLabel, vatRateBox);

        // Przycisk obliczania
        Button calculateButton = new Button("OBLICZ");

        // Wyniki
        Label resultLabel = new Label("Wyniki:");
        Label nettoLabel = new Label("Netto: ");
        Label vatLabel = new Label("VAT: ");
        Label bruttoLabel = new Label("Brutto: ");

        VBox resultBox = new VBox(5, resultLabel, nettoLabel, vatLabel, bruttoLabel);

        // Obsługa przycisku obliczania
        calculateButton.setOnAction(e -> {
            try {
                double baseValue = Double.parseDouble(baseValueField.getText());
                String vatRateString = vatRateBox.getValue().replace("%", "");
                double vatRate = Double.parseDouble(vatRateString) / 100;

                double netto, vat, brutto;

                if (nettoToBrutto.isSelected()) {
                    netto = baseValue;
                    vat = netto * vatRate;
                    brutto = netto + vat;
                } else if (bruttoToNetto.isSelected()) {
                    brutto = baseValue;
                    netto = brutto / (1 + vatRate);
                    vat = brutto - netto;
                } else {
                    netto = baseValue;
                    vat = netto * vatRate;
                    brutto = netto + vat;
                }

                nettoLabel.setText(String.format("Netto: %.2f", netto));
                vatLabel.setText(String.format("VAT: %.2f @ %.0f%%", vat, vatRate * 100));
                bruttoLabel.setText(String.format("Brutto: %.2f", brutto));

            } catch (NumberFormatException ex) {
                nettoLabel.setText("Netto: Błąd");
                vatLabel.setText("VAT: Błąd");
                bruttoLabel.setText("Brutto: Błąd");
            }
        });


        mainLayout.getChildren().addAll(methodBox, inputBox, calculateButton, resultBox);
        mainLayout.setAlignment(Pos.CENTER);


        Scene scene = new Scene(mainLayout, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}