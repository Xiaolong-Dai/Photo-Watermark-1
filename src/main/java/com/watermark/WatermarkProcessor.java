package com.watermark;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.watermark.model.Position;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Core class for handling the watermark processing logic.
 */
public class WatermarkProcessor {

    private final WatermarkOptions options;

    public WatermarkProcessor(WatermarkOptions options) {
        this.options = options;
    }

    /**
     * Executes the main watermark processing workflow.
     */
    public void process() throws IOException {
        File sourceDir = new File(options.getSourcePath());
        File outputDir = new File(sourceDir.getName() + "_watermarked");

        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        String[] supportedExtensions = {"jpg", "jpeg", "png"};
        Collection<File> imageFiles = FileUtils.listFiles(sourceDir, supportedExtensions, true);

        if (imageFiles.isEmpty()) {
            System.out.println("No supported image files found in the specified directory.");
            return;
        }

        System.out.printf("Found %d image(s) to process...\n", imageFiles.size());

        int count = 0;
        for (File imageFile : imageFiles) {
            count++;
            System.out.printf("Processing file %d of %d: %s\n", count, imageFiles.size(), imageFile.getName());
            try {
                addWatermark(imageFile, outputDir);
            } catch (IOException e) {
                System.err.println("Could not process file: " + imageFile.getName() + ". Reason: " + e.getMessage());
            }
        }
        System.out.println("Watermarking complete.");
    }

    private void addWatermark(File imageFile, File outputDir) throws IOException {
        String watermarkText = getWatermarkText(imageFile);

        BufferedImage image = ImageIO.read(imageFile);
        Graphics2D g2d = image.createGraphics();

        // Set rendering hints for better quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Configure font
        float adaptiveFontSize = options.getFontSize() * (image.getWidth() / 1920.0f);
        Font baseFont = loadBaseFont();
        Font font = baseFont.deriveFont(adaptiveFontSize);
        g2d.setFont(font);
        g2d.setColor(options.getColor());

        // Get text dimensions
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(watermarkText);
        int textHeight = fm.getAscent();

        // Calculate position
        Position pos = options.getPosition();
        int x = pos.calculateX(image.getWidth(), textWidth, options.getMargin());
        int y = pos.calculateY(image.getHeight(), textHeight, options.getMargin());

        // Apply opacity
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, options.getOpacity()));

        // Draw watermark
        g2d.drawString(watermarkText, x, y);
        g2d.dispose();

        // Save the new image
        String extension = FilenameUtils.getExtension(imageFile.getName());
        File outputFile = new File(outputDir, imageFile.getName());
        ImageIO.write(image, extension, outputFile);
    }

    private String getWatermarkText(File imageFile) {
        Date date = null;
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
            ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

            if (directory != null) {
                date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            }
        } catch (Exception e) {
            // Ignore metadata reading errors, fallback to last modified date
        }

        if (date == null) {
            // Fallback to file's last modified date
            date = new Date(imageFile.lastModified());
        }

        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private Font loadBaseFont() throws IOException {
        if (options.getFontPath() != null) {
            File fontFile = new File(options.getFontPath());
            if (fontFile.exists()) {
                try (InputStream is = Files.newInputStream(fontFile.toPath())) {
                    return Font.createFont(Font.TRUETYPE_FONT, is);
                } catch (FontFormatException e) {
                    System.err.println("Invalid font format. Using default font. Error: " + e.getMessage());
                }
            }
        }
        // Return default system font if no font is provided or if loading fails
        return new Font(Font.SANS_SERIF, Font.BOLD, 1);
    }
}
