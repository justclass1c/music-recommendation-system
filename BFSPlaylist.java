import java.util.*;

public class BFSPlaylist {

    private Map<String, List<String>> songs;
    private Map<String, List<String>> genres;
    private Map<String, List<String>> artists;

    // List of all song names for user selection
    private List<String> allSongNames;

    public BFSPlaylist() {
        songs = new HashMap<>();
        genres = new HashMap<>();
        artists = new HashMap<>();
        allSongNames = new ArrayList<>();

        // Initialize with your provided song data
        List<SongData> songList = Arrays.asList(
                new SongData("S1", "dusk", "Sonny Zero", "R&B"),
                new SongData("S2", "forgot the time", "Sonny Zero", "R&B"),
                new SongData("S3", "Sabotage", "Sonny Zero", "R&B"),
                new SongData("S4", "oxygen", "Sonny Zero", "R&B"),
                new SongData("S5", "nosebleed", "Sonny Zero", "R&B"),
                new SongData("S6", "baby", "Justin Bieber", "Pop Rap"),
                new SongData("S7", "Love yourself", "Justin Bieber", "Pop"),
                new SongData("S8", "beauty and a beat", "Justin Bieber", "Electropop"),
                new SongData("S9", "holy", "Justin Bieber", "Pop"),
                new SongData("S10", "ghost", "Justin Bieber", "Pop"),
                new SongData("S11", "Billie jean", "Michael Jackson", "R&B"),
                new SongData("S12", "Beat it", "Michael Jackson", "Rock"),
                new SongData("S13", "Smooth criminal", "Michael Jackson", "Pop"),
                new SongData("S14", "thriller", "Michael Jackson", "Disco-funk"),
                new SongData("S15", "Man in the mirror", "Michael Jackson", "Pop"),
                new SongData("S16", "whyyy", "bixby", "Indie Pop"),
                new SongData("S17", "Take Time", "Joon", "Indie Pop"),
                new SongData("S18", "distance", "bixby", "Alternative Pop"),
                new SongData("S19", "easy", "bixby", "Alternative Pop"),
                new SongData("S20", "endlessly", "bixby", "Alternative Pop"),
                new SongData("S21", "Clarity", "Tom Frane", "Indie Pop"),
                new SongData("S22", "pretty", "JVKE", "Classical"),
                new SongData("S23", "golden hour", "JVKE", "Classical"),
                new SongData("S24", "A Thousand Years", "John Michael Howell", "Classical"),
                new SongData("S25", "by my side", "Joon", "Pop"),
                new SongData("S26", "Never Gonna Give You Up", "Rick Astley", "Electronic"),
                new SongData("S27", "Goosebumps", "Travis Scott", "Trap"),
                new SongData("S28", "Sicko Mode", "Travis Scott", "Trap"),
                new SongData("S29", "Fe!n", "Travis Scott", "Trap"),
                new SongData("S30", "Sweater Weather", "The neighbourhood", "Indie"),
                new SongData("S31", "Daddy Issues", "The neighbourhood", "Indie"),
                new SongData("S32", "Softcore", "The neighbourhood", "Indie"),
                new SongData("S33", "Doja", "Central Cee", "UK Drill"),
                new SongData("S34", "Sprinter", "Central Cee", "UK Drill"),
                new SongData("S35", "Band4Band", "Central Cee", "UK Drill"),
                new SongData("S36", "Dakiti", "Bad Bunny", "Latin Trap"),
                new SongData("S37", "Titi Me Pregunto", "Bad Bunny", "Latin Trap"),
                new SongData("S38", "Mia", "Bad Bunny", "Latin Trap"),
                new SongData("S39", "Water", "Tyla", "Afrobeats"),
                new SongData("S40", "Truth or Dare", "Tyla", "Afrobeats"),
                new SongData("S41", "Jump", "Tyla", "Afrobeats"),
                new SongData("S42", "telepatia", "Kali Uchis", "Neo-Soul"),
                new SongData("S43", "moonlight", "Kali Uchis", "Neo-Soul"),
                new SongData("S44", "Never be yours", "Kali Uchis", "Neo-Soul"),
                new SongData("S45", "Back In Black", "AC/DC", "Heavy Metal"),
                new SongData("S46", "Highway to Hell", "AC/DC", "Heavy Metal"),
                new SongData("S47", "Thunderstruck", "AC/DC", "Heavy Metal"),
                new SongData("S48", "Hotel California", "The Eagles", "Country Rock"),
                new SongData("S49", "Take it Easy", "The Eagles", "Country Rock"),
                new SongData("S50", "Desperado", "The Eagles", "Country Rock")
        );

        // Build the maps
        for (SongData data : songList) {
            String songName = data.name;
            allSongNames.add(songName);

            // Song -> [genre, artist]
            List<String> neighbors = new ArrayList<>();
            neighbors.add(data.genre);
            neighbors.add(data.artist);
            songs.put(songName, neighbors);

            // Genre -> songs
            genres.computeIfAbsent(data.genre, k -> new ArrayList<>()).add(songName);
            // Artist -> songs
            artists.computeIfAbsent(data.artist, k -> new ArrayList<>()).add(songName);
        }
    }

    public List<String> generatePlaylist(String startSongName, int maxHops) {
        // Variables as per pseudocode
        Queue<String> queue = new LinkedList<>();
        List<String> visited = new ArrayList<>();
        List<String> results = new ArrayList<>();
        List<Integer> distances = new ArrayList<>();

        // Initialization
        queue.offer(startSongName);
        distances.add(0);
        visited.add(startSongName);

        // BFS loop
        while (!queue.isEmpty()) {
            // Retrieve head (simulate at(0) and remove(0))
            String currentNode = queue.poll();
            int currentDistance = distances.remove(0);

            if (currentDistance > maxHops) {
                break;
            }

            // If current node is a song and not the start, add to results
            if (songs.containsKey(currentNode) && !currentNode.equals(startSongName)) {
                results.add(currentNode);
            }

            // Determine neighbors (vertices)
            List<String> vertices = new ArrayList<>();
            if (songs.containsKey(currentNode)) {
                // Song -> genre and artist
                vertices.addAll(songs.get(currentNode));
            } else if (genres.containsKey(currentNode)) {
                // Genre -> all songs under it
                vertices.addAll(genres.get(currentNode));
            } else if (artists.containsKey(currentNode)) {
                // Artist -> all songs by them
                vertices.addAll(artists.get(currentNode));
            }
            // else vertices remains empty

            // Traverse neighbors
            for (String vertex : vertices) {
                if (!visited.contains(vertex)) {
                    visited.add(vertex);
                    queue.offer(vertex);
                    distances.add(currentDistance + 1);
                }
            }
        }

        return results;
    }

    //Inner class to hold raw song data.
    private static class SongData {
        String id; //base on pseudocode (for what?)
        String name;
        String artist;
        String genre;

        SongData(String id, String name, String artist, String genre) {
            this.id = id;
            this.name = name;
            this.artist = artist;
            this.genre = genre;
        }
    }

    public static void main(String[] args) {
        BFSPlaylist recommender = new BFSPlaylist();
        Scanner scanner = new Scanner(System.in);

        // Display all available songs
        System.out.println("===== Available Songs =====");
        List<String> allSongs = recommender.allSongNames;
        for (int i = 0; i < allSongs.size(); i++) {
            System.out.println((i + 1) + ". " + allSongs.get(i));
        }

        // User selects a song
        System.out.print("\nEnter the number of the song to start from (1-" + allSongs.size() + "): ");
        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Exiting.");
            scanner.close();
            return;
        }

        if (choice < 1 || choice > allSongs.size()) {
            System.out.println("Invalid choice. Exiting.");
            scanner.close();
            return;
        }

        

        String startSong = allSongs.get(choice - 1);
        System.out.print("Enter maximum hop count (e.g., 2): ");
        int maxHops;
        try {
            maxHops = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Using default maxHops = 3.");
            maxHops = 3;
        }

        // Generate playlist
        System.out.println("\n===== BFS Playlist (starting from \"" + startSong + "\", maxHops=" + maxHops + ") =====");
        List<String> playlist = recommender.generatePlaylist(startSong, maxHops);

        if (playlist.isEmpty()) {
            System.out.println("No recommendations found within " + maxHops + " hops.");
        } else {
            for (int i = 0; i < playlist.size(); i++) {
                System.out.println((i + 1) + ". " + playlist.get(i));
            }
        }

        scanner.close();
    }
}