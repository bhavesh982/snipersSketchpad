package com.example.sniperssketchpad;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private Canvas drawingCanvas; // Ensure this field has the @FXML annotation

    private GraphicsContext gc;
    private boolean drawMode = true;

    @FXML
    public void initialize() {
        if (drawingCanvas == null) {
            System.err.println("Error: drawingCanvas is not injected. Check FXML configuration.");
            return;
        }

        // Initialize GraphicsContext and setup drawing events
        gc = drawingCanvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        // Log message to confirm setup
        System.out.println("Canvas and GraphicsContext initialized successfully.");

        drawingCanvas.setOnMousePressed(this::startDraw);
        drawingCanvas.setOnMouseDragged(this::draw);
        drawingCanvas.setOnMouseReleased(event -> gc.closePath());
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private void handleDrawMode() {
        drawMode = true;
        gc.setStroke(Color.BLACK);  // Set drawing color
    }

    @FXML
    private void handleEraseMode() {
        drawMode = false;
        gc.setStroke(Color.WHITE);  // Set eraser color
    }

    private void startDraw(MouseEvent event) {
        if (gc != null) {
            gc.beginPath();
            gc.moveTo(event.getX(), event.getY());
            gc.stroke();
        }
    }

    private void draw(MouseEvent event) {
        if (gc != null) {
            if (drawMode) {
                gc.lineTo(event.getX(), event.getY());
            } else {
                gc.clearRect(event.getX() - 5, event.getY() - 5, 10, 10);  // Erase area
            }
            gc.stroke();
        }
    }
}
