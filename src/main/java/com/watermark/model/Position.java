package com.watermark.model;

/**
 * Enum representing the nine possible positions for the watermark on an image.
 */
public enum Position {
    TOP_LEFT,
    TOP_CENTER,
    TOP_RIGHT,
    MIDDLE_LEFT,
    MIDDLE_CENTER,
    MIDDLE_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_CENTER,
    BOTTOM_RIGHT;

    /**
     * Calculates the X coordinate for the watermark text based on the position.
     *
     * @param imageWidth   The width of the image.
     * @param textWidth    The width of the watermark text.
     * @param margin       The margin from the edge of the image.
     * @return The calculated X coordinate.
     */
    public int calculateX(int imageWidth, int textWidth, int margin) {
        switch (this) {
            case TOP_LEFT:
            case MIDDLE_LEFT:
            case BOTTOM_LEFT:
                return margin;
            case TOP_CENTER:
            case MIDDLE_CENTER:
            case BOTTOM_CENTER:
                return (imageWidth - textWidth) / 2;
            case TOP_RIGHT:
            case MIDDLE_RIGHT:
            case BOTTOM_RIGHT:
                return imageWidth - textWidth - margin;
            default:
                throw new IllegalArgumentException("Unknown position: " + this);
        }
    }

    /**
     * Calculates the Y coordinate for the watermark text based on the position.
     *
     * @param imageHeight  The height of the image.
     * @param textHeight   The ascent of the font, a good proxy for height.
     * @param margin       The margin from the edge of the image.
     * @return The calculated Y coordinate.
     */
    public int calculateY(int imageHeight, int textHeight, int margin) {
        switch (this) {
            case TOP_LEFT:
            case TOP_CENTER:
            case TOP_RIGHT:
                return textHeight + margin;
            case MIDDLE_LEFT:
            case MIDDLE_CENTER:
            case MIDDLE_RIGHT:
                return (imageHeight + textHeight) / 2;
            case BOTTOM_LEFT:
            case BOTTOM_CENTER:
            case BOTTOM_RIGHT:
                return imageHeight - margin;
            default:
                throw new IllegalArgumentException("Unknown position: " + this);
        }
    }
}
