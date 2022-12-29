package no.address;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static no.address.Util.createFileForAddresses;

public class CleanSolutionsToOnlyAddresses {

    public static void main(String[] args) throws IOException {
        System.out.println("Running clean script output");
        assert args[0] != null;
        assert args[1] != null;

        String fileName = args[0];
        System.out.println("Input file name: " + fileName);

        File file = new File(fileName);
        Pattern pattern = Pattern.compile("(?<address>.+?)(?<sep>[➜])(?<solution>.+?)");
        List<String> resultAddresses = Files.lines(file.toPath())
                .filter(Predicate.not(String::isEmpty))
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .map(matcher -> matcher.group("address").trim())
                .toList();

        String targetDirectory = args[1];
        System.out.println("Target Directory: " + targetDirectory);

        createFileForAddresses(new File(targetDirectory), resultAddresses);
    }
}
