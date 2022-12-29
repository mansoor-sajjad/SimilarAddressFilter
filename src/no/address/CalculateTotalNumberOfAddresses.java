package no.address;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculateTotalNumberOfAddresses {
    public static void main(String[] args) throws IOException {
        assert args[0] != null;
        assert args[1] != null;
        String allAddressesFileName = args[0];
        String validAddressesByStreetFileName = args[1];

        System.out.println("All Addresses from file" + allAddressesFileName);
        System.out.println("Valid addresses from file" + validAddressesByStreetFileName);
        File validAddressesByStreetFile = new File(validAddressesByStreetFileName);
        File allAddressesFile = new File(allAddressesFileName);
        Pattern pattern = Pattern.compile("(?<street>.*?)(?<housenumber>\\d+\\w*?)");

        List<String> validatedStreetNames = Files.lines(validAddressesByStreetFile.toPath())
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .map(matcher -> matcher.group("street"))
                .toList();

        List<String> allStreetNames = Files.lines(allAddressesFile.toPath())
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .map(matcher -> matcher.group("street"))
                .toList();

        long count = allStreetNames.stream().filter(validatedStreetNames::contains).count();

        System.out.println("Total number of addresses: " + count);
    }
}
