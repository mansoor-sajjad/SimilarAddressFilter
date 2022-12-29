package no.address;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SimilarAddressFilter {
    public static void main(String[] args) throws IOException {
        assert args[0] != null;
        assert args[1] != null;
        String fileName = args[0];
        String targetFileName = args[1];

        System.out.println("Reading from" + fileName);
        File file = new File(fileName);
        Pattern pattern = Pattern.compile("(?<street>.*?)(?<housenumber>\\d+\\w*?)");
        Map<String, ArrayList<Object>> byStreet = Files.lines(file.toPath())
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .collect(Collectors.groupingBy(
                        matcher -> matcher.group("street"),
                        Collectors.mapping(
                                matcher -> matcher.group("housenumber"),
                                Collector.of(ArrayList::new, (list, houseNumber) -> {
                                    if (isHouseNumberWithLetter(houseNumber)) {
                                        if (list.stream()
                                                .map(Object::toString)
                                                .filter(SimilarAddressFilter::isHouseNumberWithLetter)
                                                .map(String::length)
                                                .noneMatch(Predicate.isEqual(houseNumber.length()))) {
                                            list.add(houseNumber);
                                        }
                                    } else {
                                        if (list.stream()
                                                .map(Object::toString)
                                                .map(String::length)
                                                .noneMatch(Predicate.isEqual(houseNumber.length()))) {
                                            list.add(houseNumber);
                                        }
                                    }
                                }, (left, right) -> {
                                    left.addAll(right);
                                    return left;
                                })
                        ))
                );

        System.out.println("Writing to" + targetFileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFileName))) {
            byStreet.forEach((street, houseNumbers) -> houseNumbers.forEach(houseNumber -> {
                try {
                    writer.write(street + houseNumber);
                    writer.write("\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }
    }

    public static boolean isHouseNumberWithLetter(String houseNumber) {
        final Pattern numberLetterPattern = Pattern.compile("\\d+[a-zA-Z]");
        final Matcher matcher = numberLetterPattern.matcher(houseNumber);
        return matcher.matches();
    }
}
