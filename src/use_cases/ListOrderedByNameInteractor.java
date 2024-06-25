package use_cases;

import file_gateway.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class ListOrderedByNameInteractor implements Function<String, Result<List<String>>> {

    private final Function<String, List<String>> getAllFiles;

    public ListOrderedByNameInteractor(Function<String, List<String>> getAllFiles) {
        this.getAllFiles = getAllFiles;
    }

    @Override
    public Result<List<String>> apply(String folderName) {

        List<String> allFiles;
        try {
            allFiles = new ArrayList<>(getAllFiles.apply(folderName));
        } catch (RuntimeException runtimeException) {
            return Result.failure(runtimeException);
        }

        allFiles.sort(null);

        return Result.success(allFiles);

    }

}
