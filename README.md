# `irimo`

`irimo` is a CLI tool that extracts, transforms, and shapes personal financial data from various
institutions, facilitating later consolidation and analysis to enhance financial decision-making.

## What is `irimo`?

A personal project tackling the challenge of centralizing financial data from institutions with
limited export capabilities.

Current focus:
- Nu Colombia savings account (OCR data extraction from mobile app screenshots)

Future vision:
- Rappi Pay credit card (Colombia, OCR)
- PeoplePass (Colombia, OCR)
- Bancolombia (Colombia, CSV)

## Why `irimo`?

As someone who values financial control, I needed a solution to consolidate information from various
institutions. With many mobile banking apps lacking data extraction features, OCR became the go-to
method.

`irimo` is my attempt to create a streamlined tool for extracting and shaping financial data. Future
plans include integrating with Google Docs spreadsheets for comprehensive tracking and analysis.

Let's see how far we can take this project!

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

## Contributing

Contributions are welcome! Please submit a Pull Request.

---

**Note**: This project is in its inception phase. The tech stack and features may change, and this
README will be updated accordingly.
