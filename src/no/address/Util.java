package no.address;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Util {
    public static void createFileForAddresses(File targetFile, List<String> addresses) throws IOException {
        System.out.println("Writing file" + targetFile);
        if (targetFile.createNewFile()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile))) {
                addresses.forEach((street) -> {
                            try {
                                writer.write(street);
                                writer.write("\n");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                );
            }
        }
    }

    public static boolean isValidFile(Path path) {
        try {
            return Files.isRegularFile(path) && !Files.isHidden(path);
        } catch (IOException e) {
            return false;
        }
    }

    public static void createFilesForMap(Map<String, List<String>> resultMap, String targetDirectory) {
        System.out.println("Target Directory: " + targetDirectory);

        resultMap.forEach((k, v) -> {
            try {
                System.out.println(k + " " + v.size());
                createFileForAddresses(new File(targetDirectory + File.separator + k + ".txt"), v);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
