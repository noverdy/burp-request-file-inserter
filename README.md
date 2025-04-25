# Request File Inserter

A Burp Suite extension that allows you to insert file contents (raw or base64-encoded) directly into HTTP requests at the current cursor position in Repeater, and delete multipart/form-data boundaries with a single click.

---

## Features

- Insert file contents (raw or base64) into HTTP requests at the cursor position
- **Delete data inside a multipart/form-data boundary** at the cursor position, preserving headers and boundaries
- Supports Burp Suite Repeater tab
- User-friendly context menu integration
- Formatted, detailed logging for easier debugging and troubleshooting

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
2. Select one of the following context menu actions:
   - **Add file (raw) to current cursor position**
   - **Add file (base64) to current cursor position**
   - **Delete multipart/form-data boundary at cursor**
3. For file insertion, choose a file from your system. The file’s contents will be inserted at the current cursor position in the request.
4. For boundary deletion, the data inside the multipart boundary at the cursor will be removed, but the boundary and headers will be preserved.

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
```

The JAR will be located in `build/libs/`.

---

## Logging

- All actions and errors are logged to Burp’s output and error tabs with timestamps and log levels.
- Logs include details such as file paths, byte counts, caret positions, boundary indices, and error messages for easier debugging.

---

## Roadmap / Ideas

- Insert as hex, URL-encoded, or compressed data
- Multipart/form-data support enhancements
- Insert as HTTP parameter or header
- File preview before insertion
- User preferences for default encoding and behavior

---

## Contributing

Pull requests and feature suggestions are welcome!  
Please open an issue or submit a PR.

---

## Credits

Developed by me.  
Built with [Burp Suite Montoya API](https://portswigger.net/burp/extender/api/montoya/).
