# Photo Watermark Tool

## Overview

This project is a utility tool for batch-adding text watermarks to images based on their shooting date. Users can customize the style and position of the watermark, making it easy to add a uniform identifier to their photo collections.

## Features

- **Batch Processing**: Supports processing all images in a specified folder at once.
- **Auto-read Shooting Date**: Automatically reads the `DateTimeOriginal` tag from the image's EXIF information and uses the `YYYY-MM-DD` date as the watermark content.
- **Text Watermark Generation**: Draws the extracted date text onto the image.
- **File Output**: Saves the processed images as new files in a sub-directory to avoid overwriting the original images.

### Customization Options

- **Position**: `top-left`, `top-center`, `top-right`, `middle-left`, `center`, `middle-right`, `bottom-left`, `bottom-center`, `bottom-right` (default: `bottom-right`).
- **Font**: Path to a `.ttf` or `.otf` font file.
- **Font Size**: Integer value for the font size.
- **Color**: Hex code (e.g., `#FFFFFF`) or color name (e.g., `white`) (default: `white`).
- **Opacity**: A value from 0.0 (fully transparent) to 1.0 (fully opaque) (default: 0.7).
- **Margin**: Margin in pixels from the edge of the image (default: 10).

## Prerequisites

- Java 8 or higher
- Apache Maven

## How to Build and Run

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/Xiaolong-Dai/Photo-Watermark-1.git
    cd Photo-Watermark-1
    ```

2.  **Build the project using Maven:**
    This will compile the source code and package it into a single executable JAR file with all dependencies.
    ```bash
    mvn clean package
    ```

3.  **Run the application:**
    After the build is complete, you can find the JAR file in the `target` directory.
    ```bash
    java -jar target/photo-watermark-1.0.0-jar-with-dependencies.jar -s <path_to_your_photos_directory> [options]
    ```

## Usage

### Command-Line Interface (CLI)

```bash
java -jar target/photo-watermark-1.0.0-jar-with-dependencies.jar [arguments]
```

### Arguments

| Argument | Alias | Description | Required | Default |
|---|---|---|---|---|
| `--source` | `-s` | Path to the source image folder. | **Yes** | |
| `--font` | `-f` | Path to the font file. | No | System default |
| `--size` | `-sz` | Font size. | No | Auto-adjusted |
| `--color` | `-c` | Font color (name or hex). | No | `white` |
| `--position` | `-p` | Watermark position. | No | `bottom-right` |
| `--opacity` | `-o` | Opacity (0.0 - 1.0). | No | `0.7` |
| `--margin` | `-m` | Margin in pixels. | No | `10` |

### Example

```bash
java -jar target/photo-watermark-1.0.0-jar-with-dependencies.jar --source "D:\Photos\TripToParis" --size 36 --color "#FFFFFF" --position "bottom-right" --opacity 0.8
```

## Dependencies

- [metadata-extractor](https://github.com/drewnoakes/metadata-extractor): For reading image metadata (EXIF).
- [commons-cli](https://commons.apache.org/proper/commons-cli/): For parsing command-line arguments.
- [commons-io](https://commons.apache.org/proper/commons-io/): For file system utilities.
