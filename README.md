# CLI Application for Filesystem Operations

This CLI application performs various filesystem operations based on the provided input arguments. The application can list files ordered by name or display a filesystem tree structure. The output can be directed either to the console or a specified file.

## Usage

The application requires three input arguments:

1. **Input Directory**: The directory to be processed.
2. **Output Mode**: The output mode, either `CONSOLE` or a file path.
3. **Tree Mode (Optional)**: If provided, the application will display the filesystem tree. If not provided, it will list files ordered by name.

### Example Commands

- To list files ordered by name and output to the console:
  ```sh
  java -jar filesystem-app.jar /path/to/directory CONSOLE
  ```

- To list files ordered by name and output to a file:
  ```sh
  java -jar filesystem-app.jar /path/to/directory /path/to/output.txt
  ```

- To display the filesystem tree and output to the console:
  ```sh
  java -jar filesystem-app.jar /path/to/directory CONSOLE TREE
  ```

- To display the filesystem tree and output to a file:
  ```sh
  java -jar filesystem-app.jar /path/to/directory /path/to/output.txt TREE
  ```

## Code Explanation

The application processes the input arguments and determines the appropriate action based on their values.

### Argument Processing

The input arguments are processed and assigned to variables. If an argument is missing, the corresponding variable is set to `null`.

```java
String input = null;
try {
    input = args[0];
} catch (IndexOutOfBoundsException ignored) {
}

String output = null;
try {
    output = args[1];
} catch (IndexOutOfBoundsException ignored) {
}

String treeOrNot = null;
try {
    treeOrNot = args[2];
} catch (IndexOutOfBoundsException ignored) {
}
```

### Gateway and Interactor Initialization

A `FileGateway` instance is created, and the appropriate interactor is instantiated based on whether the tree mode argument is provided.

```java
FileGateway gateway = new FileGatewayImpl();

Function<String, Result<List<String>>> interactor =
        (treeOrNot == null) ?
                new ListOrderedByNameInteractor(gateway::getAllFiles) :
                new GetFilesystemTreeInteractor();
```

### Output Handling

The output is directed to either the console or a file based on the output mode argument.

```java
if (output.equals("CONSOLE")) {
    interactor.apply(input).getOrElse(List.of("Empty")).forEach(System.out::println);
} else {
    new WriteFilesystemToFileInteractor(gateway::writeTreeToFile, interactor).apply(input, output);
}
```

## Classes and Interfaces

### `FileGateway`

An interface for file operations.

```java
public interface FileGateway {
    List<String> getAllFiles(String directory);
    void writeTreeToFile(String directory, List<String> data);
}
```

### `FileGatewayImpl`

A concrete implementation of the `FileGateway` interface.

```java
public class FileGatewayImpl implements FileGateway {
    // Implement the methods to interact with the filesystem
}
```

### `ListOrderedByNameInteractor`

An interactor for listing files ordered by name.

```java
public class ListOrderedByNameInteractor implements Function<String, Result<List<String>>> {
    // Implementation
}
```

### `GetFilesystemTreeInteractor`

An interactor for displaying the filesystem tree.

```java
public class GetFilesystemTreeInteractor implements Function<String, Result<List<String>>> {
    // Implementation
}
```

### `WriteFilesystemToFileInteractor`

An interactor for writing the output to a file.

```java
public class WriteFilesystemToFileInteractor {
    // Implementation
}
```

## Dependencies

Ensure you have the necessary dependencies and configurations to run the application, such as the Java runtime environment.

## License

This project is licensed under the MIT License.

---

This README provides a comprehensive overview of the CLI application, including usage instructions, code explanations, and class details. It helps users understand how to operate the application and how the underlying code works.
