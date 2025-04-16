# Team Working Guidelines


## Character Encoding

**All text files MUST use UTF-8 encoding without BOM (Byte Order Mark).**

### Why UTF-8 is important:
- Ensures proper handling of special characters, international text, and symbols
- Prevents encoding-related bugs that are difficult to debug
- Maintains consistency across different operating systems

### How to configure your IDE to use UTF-8:

#### IntelliJ IDEA
1. Go to File → Settings → Editor → File Encodings
2. Set "Global Encoding" and "Project Encoding" to UTF-8
3. Set "Default encoding for properties files" to UTF-8

#### Eclipse
1. Go to Window → Preferences → General → Workspace
2. Set "Text file encoding" to "UTF-8"

#### VS Code
1. Go to Settings (File → Preferences → Settings)
2. Search for "encoding"
3. Set "Files: Encoding" to "utf8"


## File Path Handling

**Always use `java.nio.file.Path` for file paths to ensure cross-platform compatibility.**

### Wrong way:
```java
// DON'T DO THIS
String filePath = "data\\config\\settings.json";  // Windows-specific
File file = new File(filePath);
```

### Correct way:
```java
// DO THIS
Path filePath = Paths.get("data", "config", "settings.json");  // OS-independent
File file = filePath.toFile();
```

### Additional path-handling tips:
- Never hardcode directory separators (`\` or `/`)
- Use `System.getProperty("file.separator")` if you absolutely need the separator character
- For configuration files, consider using resource loading with `getClass().getResource()`
- Use relative paths whenever possible

## Line Endings

Our `.gitattributes` file is configured to normalize line endings. However:

- Don't manually change line endings
- Don't use editor features to convert line endings
- Let Git handle line ending normalization

## IDE-Specific Files

- Don't commit IDE-specific files to the repository
- Check that `.gitignore` contains entries for your IDE's files
- Common exclusions include:
  - `.idea/` (IntelliJ)
  - `.vscode/` (VS Code)
  - `.project`, `.classpath` (Eclipse)
  - `nbproject/` (NetBeans)

## Build System

We use Gradle to ensure build consistency across platforms:

- Use provided Gradle wrapper (`./gradlew` or `gradlew.bat`)
- Don't rely on IDE-specific build features
- Define all dependencies in the Gradle build files

## Resource Management

- Store resources in `src/main/resources`
- Access resources using Class.getResource() or ClassLoader.getSystemResource()
- Use try-with-resources for all I/O operations to ensure proper resource cleanup

## Common Cross-Platform Issues to Watch For

1. **Case Sensitivity**: Windows file systems are case-insensitive; Unix/Linux are case-sensitive
2. **Path Length**: Windows has a 260-character path length limit
3. **Reserved Names**: Windows has reserved file names (CON, PRN, AUX, NUL, etc.)
4. **File Locking**: Different OSes handle file locking differently
5. **Line Separators**: Text editors may display line endings differently
6. **File Permissions**: Unix/Linux have permissions that Windows doesn't
7. **Symbolic Links**: Handled differently across platforms

## JavaFX Considerations

If using JavaFX for UI:
- Use platform-independent layout managers
- Test UI on different screen resolutions and DPI settings
- Consider right-to-left language support for internationalization

## Reporting Environment-Specific Issues

When reporting issues:
1. Specify your OS and version
2. Note your IDE and version
3. Include Java version information
4. Describe the exact steps to reproduce the issue
5. If the issue is OS-specific, note that in the issue title

By following these guidelines, we can minimize cross-platform issues and ensure a smooth development experience for all team members.