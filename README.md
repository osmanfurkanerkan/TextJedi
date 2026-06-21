# TextJedi: Architecture-Driven AI Text Processor

A production-ready, highly modular desktop text processor built with Java Swing and the Google GenAI SDK. This project focuses on implementing classic software engineering design patterns to achieve strict decoupling, dynamic runtime theme rendering, and non-blocking asynchronous AI text generation.

---

## 🏗️ Architectural Blueprints & Design Patterns

The core strength of TextJedi lies in its architectural compliance. Rather than building a monolithic GUI application, the codebase strictly enforces four primary design patterns.

### 1. Abstract Factory Pattern (`/ui/factory`)

Used to decouple UI component generation from concrete look-and-feel specifications.

* `UIFactory` serves as the abstract blueprint.
* `DarkUIFactory` and `LightUIFactory` implement concrete theme factories.
* Enables instant runtime theme switching without reconstructing application frames or UI state.

### 2. Command Pattern (`/command`)

Every transactional user operation is encapsulated inside standalone command objects implementing the `Command` interface.

Examples include:

* Opening files
* Saving documents
* Invoking AI transformations
* Managing editor state

Benefits:

* Strong adherence to the Open/Closed Principle (OCP)
* New operations can be introduced without modifying controller logic
* Cleaner separation between UI actions and business logic

### 3. Thread-Safe Singleton Pattern (`/config`)

Global application configuration is centralized within the `EditorSettings` instance.

Implementation details:

* `volatile` instance reference
* Double-checked locking initialization
* Synchronized allocation path

This ensures safe initialization under concurrent execution conditions while minimizing synchronization overhead.

### 4. Iterator Pattern (`/iterator`)

Token extraction and text traversal are handled through the custom `TextIterator`.

Features:

* RegEx-driven tokenization pipeline

```regex id="0lh0ys"
[a-zA-Z0-9çğıöşüÇĞİÖŞÜ]+
```

* Standard iterable traversal interface
* Explicit position tracking through:

  * `getStartIndex()`
  * `getEndIndex()`

The implementation hides underlying text storage structures while exposing a clean iteration API.

---

## 🤖 Natively Injected Gemini AI Engine

The application integrates the native Google GenAI Java SDK (`gemini-3-flash-preview`) to perform:

* Text transformations
* Grammar correction
* Translation
* Content refactoring

All operations occur directly inside the editor workflow.

### Concurrent Execution Graph

To keep the Java Swing Event Dispatch Thread (EDT) responsive, all API communication is isolated inside a custom `SwingWorker` execution pipeline.

Benefits:

* Non-blocking UI rendering
* Responsive editor interactions
* Safe asynchronous HTTP execution

### Atomic Reversion Injector

AI-generated document updates are automatically registered within Java's native `UndoManager`.

The update pipeline:

1. Receive generated text.
2. Temporarily detach document listeners.
3. Inject updated content.
4. Register operation into UndoManager.
5. Restore listeners.

Result:

* Single-step Undo support
* Seamless Redo functionality
* Consistent editing experience for AI-generated content

---

## 📦 Tech Stack & Core Dependencies

| Component            | Technology       |
| -------------------- | ---------------- |
| Runtime              | Java 17          |
| Build System         | Maven            |
| GUI Framework        | Java Swing & AWT |
| AI SDK               | Google GenAI SDK |
| Configuration Loader | dotenv-java      |

### Maven Dependencies

```xml id="ixhq3q"
<dependency>
    <groupId>com.google.genai</groupId>
    <artifactId>google-genai</artifactId>
    <version>1.0.0</version>
</dependency>

<dependency>
    <groupId>io.github.cdimascio</groupId>
    <artifactId>dotenv-java</artifactId>
    <version>3.0.0</version>
</dependency>
```

---

## 💻 Building and Execution

### 1. Configure API Credentials

Create a `.env` file in the project root directory:

```env id="ehfobd"
GOOGLE_API_KEY=your_actual_gemini_api_key_here
```

### 2. Maven Compilation Pipeline

Compile and package the application:

```bash id="cnjlwm"
mvn clean package
```

### 3. Run the Application

Launch the generated executable JAR:

```bash id="r3o5n6"
java -jar target/TextEditor-1.0-SNAPSHOT.jar
```

---

## 🛠️ Performance Architecture Highlights

### Local Thread-Safe Autosave Daemon

A dedicated background `javax.swing.Timer` performs periodic autosave operations.

Characteristics:

* Configurable save interval
* Reads from `settings.properties`
* Non-blocking file write operations
* Minimal impact on UI responsiveness

### Custom Graphics Buffering

The custom `LineNumberView` component dynamically calculates text line positions using `FontMetrics`.

Advantages:

* Accurate line number alignment
* Smooth rendering during window resizing
* Near-zero paint jitter
* Efficient redraw performance

---

## 🎯 Key Engineering Objectives

* Strict Design Pattern Implementation
* Runtime Theme Switching
* Asynchronous AI Processing
* Undo/Redo Preservation
* Modular and Maintainable Architecture
* Responsive Swing User Experience
* Production-Oriented Code Organization

---

## 📜 License

This project is intended for educational, software engineering, and architectural demonstration purposes.
