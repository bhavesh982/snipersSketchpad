package com.example.sniperssketchpad;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class HelloController {
    @FXML
    private Canvas drawingCanvas;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private ToggleButton eraserToolToggle;
    @FXML
    private ToggleButton penToolToggle;
    @FXML
    private Slider sizeSlider;

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

        // Default tool settings
        colorPicker.setValue(Color.BLACK);
        gc.setStroke(colorPicker.getValue());
        gc.setLineWidth(sizeSlider.getValue());

        // Set up canvas event handlers
        drawingCanvas.setOnMousePressed(this::startDraw);
        drawingCanvas.setOnMouseDragged(this::draw);

        // Set up tool and size/color adjustments
        penToolToggle.setOnAction(event -> handleDrawMode());
        eraserToolToggle.setOnAction(event -> handleEraseMode());
        colorPicker.setOnAction(event -> gc.setStroke(colorPicker.getValue()));
        sizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> gc.setLineWidth(newValue.doubleValue()));
    }

    @FXML
    private void handleDrawMode() {
        drawMode = true;
        gc.setStroke(colorPicker.getValue());
        eraserToolToggle.setSelected(false);  // Ensure eraser is deselected
    }

    @FXML
    private void handleEraseMode() {
        drawMode = false;
        gc.setStroke(Color.WHITE); // Set stroke color for erasing
        penToolToggle.setSelected(false); // Ensure pen is deselected
    }
    private double lastX, lastY;

    private void startDraw(MouseEvent event) {
        if (gc != null) {
            gc.beginPath();
            gc.moveTo(event.getX(), event.getY());
            lastX = event.getX();
            lastY = event.getY();

            if (drawMode) {
                gc.setFill(colorPicker.getValue());
                gc.fillOval(event.getX() - sizeSlider.getValue() / 2,
                        event.getY() - sizeSlider.getValue() / 2,
                        sizeSlider.getValue(), sizeSlider.getValue());
            }
            gc.stroke();
        }
    }

    private void draw(MouseEvent event) {
        if (gc != null) {
            double size = sizeSlider.getValue();

            if (drawMode) {
                // Draw a line segment from the last point to the current point
                gc.setStroke(colorPicker.getValue());
                gc.setLineWidth(size);
                gc.strokeLine(lastX, lastY, event.getX(), event.getY());

                // Update the last position to the current one
                lastX = event.getX();
                lastY = event.getY();
            } else {
                // Eraser functionality
                gc.clearRect(event.getX() - size / 2, event.getY() - size / 2, size, size);
            }
        }
    }
}
