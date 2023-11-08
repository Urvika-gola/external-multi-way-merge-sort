# external-multi-way-merge-sort
This repository contains the implementation of an External Multi-Way Merge Sort algorithm, which is designed to handle sorting of large datasets that cannot fit into the main memory. 

## Overview

This project implements an external sort-merge algorithm, which is crucial when dealing with large datasets that cannot fit into the main memory. Sorting is fundamental to database systems, especially when dealing with indexed data or processing various relational and set operators.

## Technical Aspects

The core of this implementation is the custom external merge-sort algorithm, which takes into account the limitations of in-memory sorting by utilizing disk storage for intermediate sorted runs.

### Features

- **Custom Pseudo-block Handling**: The algorithm uses 'pseudo-blocks' to manage the amount of data read from disk efficiently.
- **User-defined Sorting**: Users can sort CSV files based on any attribute they choose.
- **Dynamic File Naming**: The program dynamically names intermediate sorted run files following the pattern run<p><r>.csv, where `p` is the pass number and `r` is the run number.
- **In-Memory Sorting Flexibility**: The internal sorting of runs in Pass 0 can be achieved using any sorting routine provided by the language of implementation.

### Algorithm

The sort is performed in multiple passes:
1. **Pass 0**: Create initial sorted runs.
2. **Passes 1 - n**: Merge sorted runs from the previous pass.
3. **Clean-Up**: Copy and verify the final sorted file.

### Command-Line Interface (CLI)

The program accepts command-line arguments to specify the input file, the field number for sorting, the run-length `R`, and the `W-way` merge parameter.

Example usage:
`javac MainClass.java`
`MainClass /path/to/data.csv <FIELD_NUMBER> <RUN_LENGTH_R> <WAY_W>`

## Sample Data

Below is a sample excerpt from the dataset that the program can sort:

| Product             | Type | Release Date | Transistors (million) |
|---------------------|------|--------------|-----------------------|
| AMD Athlon 64 3500+ | CPU  | 2007-02-20   | 122                   |
| AMD Athlon 200GE    | CPU  | 2018-09-06   | 4800                  |

