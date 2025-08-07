# Object-Oriented Programming GUI Chess Project

<strong>Developed by:</strong> Ian Lingo, Pedro Perez, and Shaz Momin.

## Project Overview

This project showcases our understanding of <strong>Object-Oriented Programming (OOP)</strong> by building a fully
functional chess game using Java with a <strong>Swing-based GUI</strong> entirely from scratch. <br>

The program is organized into two primary packages: <br>
* `Game`: Handles the game board, movement validation, input handling, and UI rendering. <br>
* `Piece`: Contains all chess piece classes (`Pawn`, `Rook`, `Bishop`, etc.) with piece-specific logic.

The entry point is the `GameRun.java` file within the `Game`  package, which initializes and launches the chess 
game interface. <br>

### View Full JavaDocs Online:

[Click here to open the JavaDocs](https://ian8912.github.io/ChessOOP/)

### How to Run

<ol>
    <li> Clone or download all source files from this GitHub repository.</li>
    <li> Ensure you have <strong>Java 17 or later</strong> installed</li>
    <li> Run the game from the root directory using Gradle:</li>
</ol>

#### On macOS/Linux:
<pre> ./gradlew run </pre>
#### On Windows:
<pre> ./gradlew.bat run </pre>

### Game Features

* Click and drag pieces using your mouse.
* Enforces turn order (White and Black alternate each move).
* Implements <strong>Pawn Promotion:</strong> a pawn that reaches the opponent's back rank is 
    automatically promoted to a <strong>Queen</strong>.
* A player <strong>wins</strong> by capturing the opposing <strong>King</strong>, after which the 
    program closes automatically.
* Visual board and sprite rendering is done via Java's `Graphics2D` and `BufferedImage`.

### Project Significance

This project represents our first collaborative software effort involving: <br>

<ul>
    <li> Practical use of object-oriented principles (abstraction, polymorphism, inheritance, encapsulation</li>
    <li> GUI development using Java Swing</li>
    <li> Team collaboration, version control, and file organization</li>
</ul>

Thank you for checking out this project!
