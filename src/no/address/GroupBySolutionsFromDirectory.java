package no.address;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static no.address.GroupBySolutionsForFile.makeGroupsBySolutions;
import static no.address.Util.createFilesForMap;

public class GroupBySolutionsFromDirectory {

    public static void main(String[] args) {
        System.out.println("Running invalid solutions filter");
        assert args[0] != null;
        assert args[1] != null;

        File inputPath = new File(args[0]);
        assert inputPath.isDirectory();
        System.out.println("Input path: " + inputPath);

        Map<String, List<String>> resultMap = makeGroupsBySolutions(
                Stream.of(Objects.requireNonNull(inputPath.listFiles()))
                        .map(File::toPath)
                        .filter(Util::isValidFile)
                        .flatMap(path -> {
                            try {
                                return Files.lines(path);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
        );

        createFilesForMap(resultMap, args[1]);
    }
}
