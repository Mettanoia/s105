import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

final class WriteFilesystemToFileInteractor implements BiFunction<String, String, Boolean> {

    private final BiFunction<List<String>, String, Result<Boolean>> writeTreeToFile;
    private final Function<String, Result<List<String>>> getAllFiles;

    public WriteFilesystemToFileInteractor(BiFunction<List<String>, String, Result<Boolean>> writeTreeToFile, Function<String, Result<List<String>>> getAllFiles) {
        this.writeTreeToFile = writeTreeToFile;
        this.getAllFiles = getAllFiles;
    }

    @Override
    public Boolean apply(String startingNode, String output) {

        return this.writeTreeToFile
                .apply(
                        getAllFiles.apply(startingNode).getOrElse(List.of("Empty")),
                        output
                )
                .getOrThrow();

    }

}
