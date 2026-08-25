import java.util.*;

public class MusicRecommendationSystem {

    private static final Scanner scanner = new Scanner(System.in);
    private static final String LINE ="------------------------------------------------------------";
    private static final String DOUBLE_LINE ="============================================================";

    private final MusicGraph graph = new MusicGraph();
    private final BFSRecommender recommender = new BFSRecommender();

    public static void main(String[] args) {
        new MusicRecommendationSystem().run();
    }

    // Main Menu

    private void run() {
        graph.loadDataSet();

        System.out.println();
        System.out.println(DOUBLE_LINE);
        System.out.println("            MUSIC RECOMMENDATION SYSTEM");
        System.out.println(DOUBLE_LINE);
        System.out.println("  Loaded " + graph.getSongCatalog().size() + " songs, " + graph.getGenres().size() + " genres, " + graph.getArtists().size() + " artists.");

        boolean running = true;
        while (running) {
            showMainMenu();
            int choice = readInt("  Select an option (0-5): ", 0, 5);

            switch (choice) {
                case 1 -> viewSongs();
                case 2 -> browseByGenre();
                case 3 -> browseByArtist();
                case 4 -> viewMusicGraph();
                case 5 -> getRecommendations();
                case 0 -> running = false;
            }
        }

        System.out.println();
        System.out.println("  Thank you for using the Music Recommendation System!");
        System.out.println(DOUBLE_LINE);
        scanner.close();
    }

    private void showMainMenu() {
        System.out.println();
        System.out.println(LINE);
        System.out.println("  MAIN MENU");
        System.out.println(LINE);
        System.out.println("  1. View Songs");
        System.out.println("  2. Browse Songs by Genre");
        System.out.println("  3. Browse Songs by Artist");
        System.out.println("  4. View Music Graph");
        System.out.println("  5. Next Song Queue Recommendation ");
        System.out.println("  0. Exit");
        System.out.println(LINE);
    }

    // 1.View Songs

    private void viewSongs() {
        printHeader("VIEW SONGS");
        List<String> songs = sortedKeys(graph.getSongCatalog().keySet());
        printNumberedInColumns(songs);

        while (true) {
            System.out.println();
            int choice = readSelectionWithList(
                    "  Select a song to inspect (1-" + songs.size() + ", 0 to go back, L to list again): ",
                    0, songs.size());

            if (choice == LIST_AGAIN) {
                System.out.println();
                printNumberedInColumns(songs);
                continue;
            }
            if (choice == 0) {
                return;
            }

            Song song = graph.getSongCatalog().get(songs.get(choice - 1));
            System.out.println();
            System.out.println("  Title   : " + song.getSongTitle());
            System.out.println("  Genres  : " + String.join(", ", song.getSongGenres()));
            System.out.println("  Artists : " + String.join(", ", song.getSongArtists()));
            System.out.println();
            System.out.println("  Degree (number of edges from this song): " + (song.getSongGenres().size() + song.getSongArtists().size()));
        }
    }

    // 2.Browse by Genre

    private void browseByGenre() {
        printHeader("BROWSE SONGS BY GENRE");
        browseCategory(graph.getGenres(), "genre");
    }

    // 3.Browse by Artist

    private void browseByArtist() {
        printHeader("BROWSE SONGS BY ARTIST");
        browseCategory(graph.getArtists(), "artist");
    }

    private void browseCategory(Map<String, List<String>> category, String label) {
        List<String> keys = sortedKeys(category.keySet());
        printNumberedInColumns(keys);

        while (true) {
            System.out.println();
            int choice = readSelectionWithList(
                    "  Select a " + label + " (1-" + keys.size() + ", 0 to go back, L to list again): ",
                    0, keys.size());

            if (choice == LIST_AGAIN) {
                System.out.println();
                printNumberedInColumns(keys);
                continue;
            }
            if (choice == 0) {
                return;
            }

            String selected = keys.get(choice - 1);
            List<String> songs = sortedKeys(category.get(selected));

            System.out.println();
            System.out.println("  Songs under " + label + " \"" + selected + "\" (" + songs.size() + "):");
            for (int i = 0; i < songs.size(); i++) {
                System.out.println("     " + (i + 1) + ". " + songs.get(i));
            }
        }
    }

    // 4.View Music Graph

    private void viewMusicGraph() {
        printHeader("MUSIC GRAPH - ADJACENCY LIST");
        System.out.println("  Which part of the graph do you want to display?");
        System.out.println("     1. Song    -> Genres / Artists");
        System.out.println("     2. Genre   -> Songs");
        System.out.println("     3. Artist  -> Songs");
        System.out.println("     0. Back to main menu");
        System.out.println();

        int choice = readInt("  Select an option (0-3): ", 0, 3);
        System.out.println();

        switch (choice) {
            case 1 -> {
                List<String> header = new ArrayList<>();
                header.add(String.format("  %-24s | %-27s | %s", "SONG", "GENRES", "ARTISTS"));
                header.add("  " + "-".repeat(24) + "-+-" + "-".repeat(27) + "-+-" + "-".repeat(30));

                List<String> rows = new ArrayList<>();
                for (String title : sortedKeys(graph.getSongCatalog().keySet())) {
                    Song song = graph.getSongCatalog().get(title);
                    rows.add(String.format("  %-24s | %-27s | %s",
                            title,
                            String.join(", ", song.getSongGenres()),
                            String.join(", ", song.getSongArtists())));
                }
                printPaged(rows, header);
                printEdgeCount();
                pause();
            }
            case 2 -> {
                printAdjacency(graph.getGenres(), "GENRE -> SONGS");
                pause();
            }
            case 3 -> {
                printAdjacency(graph.getArtists(), "ARTIST -> SONGS");
                pause();
            }
            case 0 -> { /* back to main menu */ }
        }
    }

    private void printAdjacency(Map<String, List<String>> map, String heading) {
        System.out.println("  " + heading);
        System.out.println("  " + LINE);
        List<String> rows = new ArrayList<>();
        for (String key : sortedKeys(map.keySet())) {
            List<String> songs = map.get(key);
            rows.add("  " + key + "  (" + songs.size() + (songs.size() == 1 ? " song)" : " songs)"));
            rows.add("      -> " + String.join(", ", songs));
        }
        printPaged(rows);
    }

    private void printPaged(List<String> lines) {
        printPaged(lines, new ArrayList<>());
    }

    private void printPaged(List<String> lines, List<String> repeatingHeader) {
        final int ROWS_PER_PAGE = 20;
        int total = lines.size();
        int totalPages = Math.max(1, (total + ROWS_PER_PAGE - 1) / ROWS_PER_PAGE);

        for (int page = 0; page < totalPages; page++) {
            int from = page * ROWS_PER_PAGE;
            int to = Math.min(from + ROWS_PER_PAGE, total);

            String label = "[  PAGE " + (page + 1) + " OF " + totalPages + "  ]";
            int padding = LINE.length() - label.length();
            int left = padding / 2;
            System.out.println();
            System.out.println("  " + "=".repeat(Math.max(0, left)) + label + "=".repeat(Math.max(0, padding - left)));
            System.out.println();

            for (String headerLine : repeatingHeader) {
                System.out.println(headerLine);
            }
            for (int i = from; i < to; i++) {
                System.out.println(lines.get(i));
            }

            System.out.println();
            System.out.println("  " + LINE);
            String position = "  Showing " + (from + 1) + "-" + to + " of " + total;

            if (page < totalPages - 1) {
                String cmd = readLine(position + "   [ENTER] next page   [Q] stop : ");
                if (cmd.equalsIgnoreCase("q")) {
                    System.out.println("  (display stopped early)");
                    return;
                }
            } else {
                System.out.println(position + "   -- END OF LIST --");
            }
        }
    }

    private void printEdgeCount() {
        int edges = 0;
        for (Song song : graph.getSongCatalog().values()) {
            edges += song.getSongGenres().size() + song.getSongArtists().size();
        }
        int vertices = graph.getSongCatalog().size() + graph.getGenres().size() + graph.getArtists().size();
        System.out.println("  " + LINE);
        System.out.println("  Vertices (V) = " + vertices + "   Edges (E) = " + edges);
    }

    // 5.Recommendations (BFS)

    private void getRecommendations() {
        printHeader("NEXT SONG TO QUEUE RECOMMENDATIONS");
        List<String> masterSongs = sortedKeys(graph.getSongCatalog().keySet());

        List<String> activeList = masterSongs;
        boolean fromRecommendations = false;
        Integer depth = null; 
        printNumberedInColumns(activeList);

        while (true) {
            System.out.println();
            String promptLabel = fromRecommendations
                    ? "Select the next song to queue"
                    : "Select the currently playing song";
            String depthHint = (depth != null) ? ", D to change depth" : "";
            int choice = readSelectionWithList(
                    "  " + promptLabel + " (1-" + activeList.size() + ", 0 to go back, L to list again" + depthHint + "): ",
                    0, activeList.size(), depth != null);

            if (choice == CHANGE_DEPTH) {
                System.out.println();
                System.out.println("  Current depth: " + depth);
                depth = readInt("  Enter new recommendation depth (1-5): ", 1, 5);
                System.out.println("  Depth updated to " + depth + ".");
                continue;
            }
            if (choice == LIST_AGAIN) {
                System.out.println();
                printNumberedInColumns(activeList);
                continue;
            }
            if (choice == 0) {
                return;
            }

            String startSong = activeList.get(choice - 1);

            System.out.println();
            System.out.println("  Currently playing: " + startSong);

            if (depth == null) {
                System.out.println();
                System.out.println("  Recommendation depth controls.");
                System.out.println("     1 = songs by the same artist or in the same genre");
                System.out.println("     2 = also songs linked through those songs");
                System.out.println("     (higher = more results, but less accurate)");
                System.out.println();
                depth = readInt("  Enter recommendation depth (1-5): ", 1, 5);
            }

            // One recommendation level = 2 graph hops
            int maxHops = depth * 2;

            List<String> recommendations = recommender.recommend(graph, startSong, maxHops);

            System.out.println();
            System.out.println("  " + LINE);
            System.out.println("  RECOMMENDATIONS based on \"" + startSong + "\"  (depth " + depth + ")");
            System.out.println("  " + LINE);

            if (recommendations.isEmpty()) {
                System.out.println("  No recommendations found for this song.");
                System.out.println("  Returning to the full song list.");
                activeList = masterSongs;
                fromRecommendations = false;
                System.out.println();
                printNumberedInColumns(activeList);
            } else {
                for (int i = 0; i < recommendations.size(); i++) {
                    System.out.println("     " + (i + 1) + ". " + recommendations.get(i));
                }
                System.out.println();
                System.out.println("  " + recommendations.size() + " songs found (closest matches listed first).");
                activeList = recommendations;
                fromRecommendations = true;
            }
        }
    }

private List<String> sortedKeys(Collection<String> keys) {
        List<String> sorted = new ArrayList<>(keys);
        sorted.sort(String.CASE_INSENSITIVE_ORDER);
        return sorted;
    }

    private void printNumberedInColumns(List<String> items) {
        int total = items.size();
        int half = (total + 1) / 2;
        for (int i = 0; i < half; i++) {
            String left = String.format("%2d. %-30s", i + 1, items.get(i));
            String right = "";
            if (i + half < total) {
                right = String.format("%2d. %s", i + half + 1, items.get(i + half));
            }
            System.out.println("  " + left + right);
        }
    }

    private void printHeader(String heading) {
        System.out.println();
        System.out.println(LINE);
        System.out.println("  " + heading);
        System.out.println(LINE);
    }

    private void pause() {
        readLine("\n  Press ENTER to return to the main menu...");
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            System.out.println();
            System.out.println("  Input stream closed. Exiting.");
            scanner.close();
            System.exit(0);
        }
        return scanner.nextLine().trim();
    }

    private static void clearPreviousLine() {
        System.out.print("\033[1A\033[2K\r");
    }

    private static String invalidPrompt(String basePrompt) {
        String trimmed = basePrompt.replaceAll("[:\\s]+$", "");
        return trimmed + " [invalid, try again]: ";
    }

    private static int readInt(String prompt, int min, int max) {
        String currentPrompt = prompt;
        while (true) {
            String input = readLine(currentPrompt);
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    clearPreviousLine();
                    currentPrompt = invalidPrompt(prompt);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                clearPreviousLine();
                currentPrompt = invalidPrompt(prompt);
            }
        }
    }

    private static final int LIST_AGAIN = -1;

    private static final int CHANGE_DEPTH = -2;

    private static int readSelectionWithList(String prompt, int min, int max) {
        return readSelectionWithList(prompt, min, max, false);
    }

    private static int readSelectionWithList(String prompt, int min, int max, boolean allowDepthChange) {
        String currentPrompt = prompt;
        while (true) {
            String input = readLine(currentPrompt);
            if (input.equalsIgnoreCase("l")) {
                return LIST_AGAIN;
            }
            if (allowDepthChange && input.equalsIgnoreCase("d")) {
                return CHANGE_DEPTH;
            }
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    clearPreviousLine();
                    currentPrompt = invalidPrompt(prompt);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                clearPreviousLine();
                currentPrompt = invalidPrompt(prompt);
            }
        }
    }
}
