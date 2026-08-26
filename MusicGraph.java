import java.util.*;
import java.io.*;

public class MusicGraph {
    private Map<String, Song> songCatalog = new HashMap<>(); // stores Song objects
    private Map<String, List<String>> genres = new HashMap<>();
    private Map<String, List<String>> artists = new HashMap<>();

    public void addSong() {
        System.out.print("Enter a song title: ");

    }

    //load data
    public void loadSong(String songTitle, List<String> songGenres, List<String> songArtists) {
        Song song = new Song(songTitle, songGenres, songArtists);
        songCatalog.put(songTitle, song);

        for (String genre : songGenres) {
            genres.computeIfAbsent(genre, k -> new ArrayList<>()).add(songTitle);
        }
        for (String artist : songArtists) {
            artists.computeIfAbsent(artist, k -> new ArrayList<>()).add(songTitle);
        }
    }

    public void loadDataSet() {
        String fileName = "songs.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length != 3) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                String title = parts[0].trim();
                List<String> genres = Arrays.stream(parts[1].split(",")).map(String::trim).toList();
                List<String> artists = Arrays.stream(parts[2].split(",")).map(String::trim).toList();

                loadSong(title, genres, artists);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + fileName + " not found.");
        } catch (IOException e) {
            System.out.println("Error reading " + fileName + ": " + e.getMessage());
        }
    }

    // ---------- Add / Remove ----------

    public boolean addSong(String songTitle, List<String> songGenres, List<String> songArtists) {
        if (songTitle == null || songTitle.isBlank()) {
            System.out.println("  Error: Song title cannot be empty.");
            return false;
        }
        if (songCatalog.containsKey(songTitle)) {
            System.out.println("  Error: A song titled \"" + songTitle + "\" already exists.");
            return false;
        }
        if (songGenres == null || songGenres.isEmpty()) {
            System.out.println("  Error: At least one genre is required.");
            return false;
        }
        if (songArtists == null || songArtists.isEmpty()) {
            System.out.println("  Error: At least one artist is required.");
            return false;
        }

        Song song = new Song(songTitle, songGenres, songArtists);
        songCatalog.put(songTitle, song);

        for (String genre : songGenres) {
            genres.computeIfAbsent(genre, k -> new ArrayList<>()).add(songTitle);
        }
        for (String artist : songArtists) {
            artists.computeIfAbsent(artist, k -> new ArrayList<>()).add(songTitle);
        }

        saveDataSet();
        reloadFromDisk();
        return true;
    }

    public boolean removeSong(String songTitle) {
        if (!songCatalog.containsKey(songTitle)) {
            System.out.println("  Error: Song \"" + songTitle + "\" not found.");
            return false;
        }

        Song song = songCatalog.get(songTitle);

        for (String genre : song.getSongGenres()) {
            List<String> list = genres.get(genre);
            if (list != null) {
                list.remove(songTitle);
                if (list.isEmpty()) {
                    genres.remove(genre);
                }
            }
        }
        for (String artist : song.getSongArtists()) {
            List<String> list = artists.get(artist);
            if (list != null) {
                list.remove(songTitle);
                if (list.isEmpty()) {
                    artists.remove(artist);
                }
            }
        }

        songCatalog.remove(songTitle);
        saveDataSet();
        reloadFromDisk();
        return true;
    }

    // ---------- Persistence ----------

    private void saveDataSet() {
        String fileName = "songs.txt";
        List<String> titles = new ArrayList<>(songCatalog.keySet());
        titles.sort(String.CASE_INSENSITIVE_ORDER);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String title : titles) {
                Song song = songCatalog.get(title);
                String line = title + "|"
                        + String.join(",", song.getSongGenres()) + "|"
                        + String.join(",", song.getSongArtists());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("  Error saving data: " + e.getMessage());
        }
    }

    private void reloadFromDisk() {
        songCatalog.clear();
        genres.clear();
        artists.clear();
        loadDataSet();
    }

    // ---------- Getter（for BFSRecommender to use） ----------
    public Map<String, Song> getSongCatalog() {
        return songCatalog;
    }

    public Map<String, List<String>> getGenres() {
        return genres;
    }

    public Map<String, List<String>> getArtists() {
        return artists;
    }
}
