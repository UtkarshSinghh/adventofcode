package org.utkarsh.adventofcode;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class AdventOfCode {

  public static void main(String[] args) throws Exception {
    String apiUrl = "https://adventofcode.com/2024/day/1/input";
    String fetchedData = fetchData(apiUrl);

    List<Integer> list1 = new ArrayList<>();
    List<Integer> list2 = new ArrayList<>();

    String[] dataLines = fetchedData.split("\n");

    for (String line : dataLines) {
      String[] locationIdsInEachLine = line.trim().split("\\s+");
      list1.add(Integer.parseInt(locationIdsInEachLine[0].trim()));
      list2.add(Integer.parseInt(locationIdsInEachLine[1].trim()));
    }

    similarityScore(list1, list2);
  }

  private static void diffSum(List<Integer> list1, List<Integer> list2) {
    list1.sort(null);
    list2.sort(null);

    int list1Size = list1.size();
    int list2Size = list2.size();
    int currIndex = 0;

    long diffSum = 0l;

    while (currIndex < list1Size && currIndex < list2.size()) {
      diffSum += (long) Math.abs(list1.get(currIndex) - list2.get(currIndex));
      currIndex++;
    }

    System.out.println("Total difference = " + diffSum);
  }

  private static void similarityScore(List<Integer> list1, List<Integer> list2) {
    Map<Integer, Integer> countOccurence = new HashMap<>();
    for (Integer ele : list2) {
      Integer currVal = countOccurence.get(ele);
      if (currVal == null) {
        countOccurence.put(ele, 1);
      } else {
        countOccurence.put(ele, currVal + 1);
      }
    }

    long similarityScore = 0l;

    for (Integer ele : list1) {
      Integer occurencesInList2 = countOccurence.get(ele);
      if (occurencesInList2 != null) {
        similarityScore += (ele * occurencesInList2);
      }
    }

    System.out.println("sismilarity score = " + similarityScore);
  }

  // Method to fetch data from API
  private static String fetchData(String apiUrl) throws Exception {
    URL url = new URL(apiUrl);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");

    // Add required headers
    conn.setRequestProperty(
        "Cookie",
        "session=53616c7465645f5fcaaf1186fe963f9714314e5c75ccca7257ef334a446dcfaf3194aa24293c345a0fa895e4c309a1d40b2aac6804ccf424ff8fc2e5f590054a");

    // Check for a successful response
    int responseCode = conn.getResponseCode();
    if (responseCode != HttpURLConnection.HTTP_OK) {
      throw new RuntimeException("Failed to fetch data. HTTP error code: " + responseCode);
    }

    // Handle the response stream, decompressing if necessary
    InputStream inputStream = conn.getInputStream();
    String contentEncoding = conn.getHeaderField("Content-Encoding");
    if ("gzip".equalsIgnoreCase(contentEncoding)) {
      inputStream = new GZIPInputStream(inputStream); // Decompress gzip stream
    }

    // Read the decompressed response
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    StringBuilder response = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      response.append(line).append("\n");
    }
    reader.close();
    conn.disconnect();

    return response.toString();
  }
}
