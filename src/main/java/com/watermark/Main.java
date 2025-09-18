package com.watermark;

import com.watermark.model.Position;
import org.apache.commons.cli.*;

import java.awt.*;
import java.io.File;

/**
 * Main entry point for the photo-watermark application.
 */
public class Main {

    public static void main(String[] args) {
        Options cliOptions = buildCliOptions();
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(cliOptions, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            formatter.printHelp("add-watermark", cliOptions);
            System.exit(1);
            return;
        }

        if (!cmd.hasOption("source")) {
            formatter.printHelp("add-watermark", cliOptions);
            System.exit(1);
            return;
        }

        try {
            WatermarkOptions watermarkOptions = parseWatermarkOptions(cmd);
            WatermarkProcessor processor = new WatermarkProcessor(watermarkOptions);
            processor.process();
        } catch (Exception e) {
            System.err.println("An error occurred during processing: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static Options buildCliOptions() {
        Options options = new Options();

        options.addOption(Option.builder("s").longOpt("source")
                .hasArg()
                .required()
                .desc("Source image directory (can be a relative or absolute path) (required)")
                .build());

        options.addOption(Option.builder("f").longOpt("font")
                .hasArg()
                .desc("Path to font file (.ttf, .otf)")
                .build());

        options.addOption(Option.builder("sz").longOpt("size")
                .hasArg()
                .desc("Font size (default: 36)")
                .build());

        options.addOption(Option.builder("c").longOpt("color")
                .hasArg()
                .desc("Font color in hex (e.g., #FFFFFF) or name (e.g., white) (default: white)")
                .build());

        options.addOption(Option.builder("p").longOpt("position")
                .hasArg()
                .desc("Watermark position (e.g., bottom-right, top-center) (default: bottom-right)")
                .build());

        options.addOption(Option.builder("o").longOpt("opacity")
                .hasArg()
                .desc("Opacity from 0.0 to 1.0 (default: 0.7)")
                .build());

        options.addOption(Option.builder("m").longOpt("margin")
                .hasArg()
                .desc("Margin in pixels (default: 10)")
                .build());

        return options;
    }

    private static WatermarkOptions parseWatermarkOptions(CommandLine cmd) {
        String sourcePath = cmd.getOptionValue("source");
        File sourceDir = new File(sourcePath);
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new IllegalArgumentException("Invalid source path: " + sourcePath);
        }

        WatermarkOptions options = new WatermarkOptions(sourcePath);

        if (cmd.hasOption("font")) {
            options.setFontPath(cmd.getOptionValue("font"));
        }
        if (cmd.hasOption("size")) {
            options.setFontSize(Integer.parseInt(cmd.getOptionValue("size")));
        }
        if (cmd.hasOption("color")) {
            options.setColor(parseColor(cmd.getOptionValue("color")));
        }
        if (cmd.hasOption("position")) {
            try {
                options.setPosition(Position.valueOf(cmd.getOptionValue("position").toUpperCase().replace('-', '_')));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid position value. Use one of: top-left, bottom-right, etc.");
            }
        }
        if (cmd.hasOption("opacity")) {
            options.setOpacity(Float.parseFloat(cmd.getOptionValue("opacity")));
        }
        if (cmd.hasOption("margin")) {
            options.setMargin(Integer.parseInt(cmd.getOptionValue("margin")));
        }

        return options;
    }

    private static Color parseColor(String colorStr) {
        if (colorStr.startsWith("#")) {
            return Color.decode(colorStr);
        } else {
            try {
                return (Color) Color.class.getField(colorStr.toLowerCase()).get(null);
            } catch (Exception e) {
                System.err.println("Invalid color name '" + colorStr + "'. Using default.");
                return Color.WHITE;
            }
        }
    }
}
