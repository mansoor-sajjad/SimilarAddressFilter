package no.address;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static no.address.Util.createFilesForMap;

public class GroupBySolutionsForFile {

    public static void main(String[] args) throws IOException {
        System.out.println("Running Group by solutions script");
        assert args[0] != null;
        assert args[1] != null;

        String fileName = args[0];
        System.out.println("Input file name: " + fileName);

        Map<String, List<String>> resultMap = makeGroupsBySolutions(Files.lines(new File(fileName).toPath()));

        createFilesForMap(resultMap, args[1]);
    }

    public static Map<String, List<String>> makeGroupsBySolutions(Stream<String> lines) {
        Pattern pattern = Pattern.compile("(?<address>.+?)(?<sep>[➜])(?<solution>.+?)");
        return lines
                .filter(Predicate.not(String::isEmpty))
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .collect(Collectors.groupingBy(
                        matcher -> {
                            String solution = matcher.group("solution").trim();
                            if (solution.equals("Valid")) {
                                return "valid";
                            } else if (solution.equals("No Solution")) {
                                return "no_solution";
                            } else if (solution.contains("locality")) {
                                return "locality";
                            } else if (solution.contains("county")) {
                                return "county";
                            } else if (solution.contains("country")) {
                                return "country";
                            } else if (solution.contains("region")) {
                                return "region";
                            } else if (solution.contains("postcode")) {
                                return "postcode";
                            } else if (solution.contains("venue")) {
                                return "venue";
                            } else if (solution.contains("street")) {
                                return "street";
                            }
                            return "others";
                        },
                        Collectors.mapping(
                                matcher -> matcher.group("address").trim(),
                                Collectors.toList())
                ));
    }
}
