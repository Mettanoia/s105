import java.util.List;

public interface FileGateway {
    List<String> getAllFiles(String folderName);
    Result<Boolean> writeTreeToFile(List<String> fileTree, String fileName);
}
