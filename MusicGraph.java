import java.util.*;


public class MusicGraph {
    private Map<String, List<String>> users = new HashMap<>();
    private Map<String, List<String>> songs = new HashMap<>();
    private Map<String, List<String>> genres = new HashMap<>();
    private Map<String, List<String>> artists = new HashMap<>();

    private Map<String, Song> songCatalog = new HashMap<>(); // stores Song objects
}
