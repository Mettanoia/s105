import java.io.*;
import java.util.Arrays;
import java.util.List;

final class FileGatewayImpl implements FileGateway {

    @Override
    public List<String> getAllFiles(String folderName) {
        if (folderName == null) throw new IllegalArgumentException("folderName cannot be null.");

        File folder = new File(folderName);
        if (!folder.exists()) throw new IllegalArgumentException("folderName doesn't exist.");
        if (!folder.isDirectory()) throw new IllegalArgumentException("folderName must be a folder.");

        File[] folderContent = folder.listFiles();

        assert folderContent != null;
        return Arrays.stream(folderContent)
                .map(file -> (!file.isDirectory()) ? file.getName() : file.getName() + "/")
                .toList();

    }

    @Override
    public Result<Boolean> writeTreeToFile(List<String> fileTree, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String line : fileTree) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            return Result.failure(new RuntimeException(e.getMessage(), e));
        }
        return Result.success(true);
    }


}

