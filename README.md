# `irimo`

`irimo` is a CLI tool that consolidates and transforms personal financial data from various
institutions, providing a comprehensive view for better financial decision-making.

## What is `irimo`?

A personal project to centralize financial data management across multiple institutions, overcoming
limitations of platforms without easy data export options.

## Why `irimo`?

- Gathers transactions from various financial institutions
- Handles data extraction, even without direct export
- Consolidates information efficiently
- Provides quick access to aggregated data

## Name Origin

Named after "Irimo" by Dimensión Latina, featuring Oscar D'León (
1975). [Listen on Spotify](https://open.spotify.com/track/3B2hIQoszbVoiUfiQMC8ra?si=c6d99421c1e94195)

## Features

- **Multi-source Data Extraction**: Pull data from various personal financial institutions
- **OCR Superpowers**: Handle non-exportable data sources using screenshots
- **Data Shapeshifting**: Fast and efficient data consolidation
- **Clipboard Convenience**: Quick access to consolidated data via clipboard
- **Google Sheets Integration** (Future Feature): Direct integration with Google Sheets

## Quick Start

Want to give `irimo` a try? Currently, only `jbang` is supported for quick installation.

### Prerequisites

1. Install jbang (see [jbang installation instructions](https://www.jbang.dev/documentation/guide/latest/installation.html))

### Installation

```bash
jbang app install irimo@garodriguezlp
```

> **Note:** `irimo` has been tested only on macOS with M1 chip. It might work on Windows, but this is untested.
> Due to Tesseract dependencies, it may not work on most Unix environments without additional setup.
> Learn more about Tesseract compatibility [here](https://tess4j.sourceforge.net/usage.html).
>
> Required libraries are embedded for macOS (M1). For Windows and other environments, additional
> setup may be necessary.

## Tech Stack

`irimo` leverages a robust set of technologies:

- [Spring Boot](https://spring.io/projects/spring-boot): For creating stand-alone, production-grade
  Spring-based applications
- [picocli](https://picocli.info): A powerful framework for creating Java command line applications
- [Tess4j](https://github.com/nguyenq/tess4j): A Java JNA wrapper for Tesseract OCR API
- [jreleaser](https://jreleaser.org/): For streamlined Java project releases

## Design and Distribution

For design insights, check [design notes](notes/design.md). Distribution options include jreleaser
and jbang support, with Docker encapsulation being considered.
See [jreleaser notes](notes/jreleaser.md) for details.

## Getting Started

(Instructions to be added as the project progresses)

## Contributing

Contributions are welcome! Please submit a Pull Request.

---

**Note**: This project is in its inception phase. The tech stack and features may change, and this
README will be updated accordingly.
