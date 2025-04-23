# Request File Inserter

A Burp Suite extension that allows you to insert file contents (raw or base64-encoded) directly into HTTP requests at the current cursor position in Repeater.

---

## Features

- Insert file contents (raw or base64) into HTTP requests at the cursor position
- Supports Burp Suite Repeater tab
- User-friendly context menu integration
- Formatted logging for easier debugging and troubleshooting

---

## Installation

1. **Download the latest release:**

    - Go to the [Releases](https://github.com/noverdy/burp-request-file-inserter/releases) page.
    - Download the latest `RequestFileInserter.jar` file.

2. **Load into Burp Suite:**

    - Open Burp Suite
    - Go to the **Extender** tab
    - Click **Add**
    - Select **Extension type: Java**
    - Choose the downloaded JAR file

---

## Usage

1. In the **Repeater** tab, right-click inside the request editor.
2. Select:
    - **Add file (raw) to current cursor position**  
      _or_
    - **Add file (base64) to current cursor position**
3. Choose a file from your system.
4. The file’s contents will be inserted at the current cursor position in the request.

---

## Development

If you want to build from source or contribute, see instructions below.

### Requirements

- Java 21
- Gradle (or compatible build tool)
- Burp Suite (Community or Professional)

### Building

```sh
./gradlew jar
