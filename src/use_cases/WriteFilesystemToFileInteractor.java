package use_cases;

import file_gateway.Result;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class WriteFilesystemToFileInteractor implements BiFunction<String, String, Boolean> {

    private final BiFunction<List<String>, String, Result<Boolean>> writeTreeToFile;
    private final Function<String, Result<List<String>>> getAllFiles;

    public WriteFilesystemToFileInteractor(BiFunction<List<String>, String, Result<Boolean>> writeTreeToFile, Function<String, Result<List<String>>> getAllFiles) {
        this.writeTreeToFile = writeTreeToFile;
        this.getAllFiles = getAllFiles;
    }

    @Override
    public Boolean apply(String startingNode, String output) {

        try {
            return this.writeTreeToFile
                    .apply(
                            getAllFiles.apply(startingNode).getOrElse(List.of("Empty")),
                            output
                    )
                    .getOrThrow();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

}
