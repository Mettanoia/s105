package use_cases;

import file_gateway.Result;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.nio.file.FileVisitResult.CONTINUE;

public final class GetFilesystemTreeInteractor implements Function<String, Result<List<String>>> {

    @Override
    public Result<List<String>> apply(String startingNode) {
        if (startingNode == null)
            return Result.failure(new IllegalArgumentException("startingNode cannot be null."));

        Path startingNodePath;
        try {
            startingNodePath = Paths.get(startingNode);
        } catch (InvalidPathException e) {
            return Result.failure(e);
        }

        InnerFileWalker innerFileWalker = null;
        List<String> resultingTree = new ArrayList<>(0);
        try {
            innerFileWalker = new InnerFileWalker(resultingTree);
            Files.walkFileTree(startingNodePath, innerFileWalker);
        } catch (IOException e) {
            return Result.failure(new RuntimeException(e.getMessage(), e));
        }

        return Result.success(resultingTree);

    }

    private static class InnerFileWalker extends SimpleFileVisitor<Path> {

        private final List<String> resultingTree;
        private int indentLevel = 0;
        private final String indentation = "    ";

        private InnerFileWalker(List<String> resultingTree) throws IOException {
            this.resultingTree = resultingTree;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {

            resultingTree.add(indentation.repeat(indentLevel) + dir.getFileName() + "/    " + attrs.lastModifiedTime());
            indentLevel++;
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

            resultingTree.add(indentation.repeat(indentLevel) + file.getFileName() + "/    " + attrs.lastModifiedTime());
            return CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            indentLevel--;
            return CONTINUE;
        }

    }

}
