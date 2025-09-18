package com.watermark;

import com.watermark.model.Position;

import java.awt.*;

/**
 * A data class to hold all watermark configuration options.
 */
public class WatermarkOptions {

    private final String sourcePath;
    private String fontPath = null; // Path to a .ttf or .otf file
    private int fontSize = 36;
    private Color color = Color.WHITE;
    private Position position = Position.BOTTOM_RIGHT;
    private float opacity = 0.7f;
    private int margin = 10;

    public WatermarkOptions(String sourcePath) {
        if (sourcePath == null || sourcePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Source path cannot be null or empty.");
        }
        this.sourcePath = sourcePath;
    }

    // Getters and Setters

    public String getSourcePath() {
        return sourcePath;
    }

    public String getFontPath() {
        return fontPath;
    }

    public void setFontPath(String fontPath) {
        this.fontPath = fontPath;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        if (fontSize > 0) {
            this.fontSize = fontSize;
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (color != null) {
            this.color = color;
        }
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        if (position != null) {
            this.position = position;
        }
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        if (opacity >= 0.0f && opacity <= 1.0f) {
            this.opacity = opacity;
        }
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        if (margin >= 0) {
            this.margin = margin;
        }
    }
}
