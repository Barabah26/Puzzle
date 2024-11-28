import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Puzzle {
    public static void main(String[] args) {
        String filePath = "source.txt";

        try {
            List<String> fragments = readFragmentsFromFile(filePath);
            String longestSequence = findLongestSequence(fragments);
            System.out.println("Найдовша послідовність: " + longestSequence);
            System.out.println("Довжина максимальної послідовності в символах: " + longestSequence.length());
        } catch (IOException e) {
            System.err.println("Помилка читання файлу: " + e.getMessage());
        }
    }

    public static List<String> readFragmentsFromFile(String filePath) throws IOException {
        List<String> fragments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && line.matches("\\d+")) {
                    fragments.add(line);
                }
            }
        }
        return fragments;
    }

    public static String findLongestSequence(List<String> fragments) {
        Map<String, List<String>> adjacencyMap = buildGraph(fragments);
        String longestSequence = "";

        for (String fragment : fragments) {
            Set<String> visited = new HashSet<>();
            String currentSequence = dfs(fragment, adjacencyMap, visited);
            if (currentSequence.length() > longestSequence.length()) {
                longestSequence = currentSequence;
            }
        }

        return longestSequence;
    }

    private static Map<String, List<String>> buildGraph(List<String> fragments) {
        Map<String, List<String>> adjacencyMap = new HashMap<>();

        for (String fragment : fragments) {
            String suffix = fragment.substring(fragment.length() - 2);
            adjacencyMap.putIfAbsent(fragment, new ArrayList<>());

            for (String other : fragments) {
                if (!fragment.equals(other)) {
                    String prefix = other.substring(0, 2);
                    if (suffix.equals(prefix)) {
                        adjacencyMap.get(fragment).add(other);
                    }
                }
            }
        }

        return adjacencyMap;
    }

    private static String dfs(String current, Map<String, List<String>> graph, Set<String> visited) {
        visited.add(current);
        String longestPath = current;

        for (String neighbor : graph.getOrDefault(current, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                String path = dfs(neighbor, graph, visited);
                String mergedPath = current + path.substring(2);
                if (mergedPath.length() > longestPath.length()) {
                    longestPath = mergedPath;
                }
            }
        }

        visited.remove(current);
        return longestPath;
    }
}
