import java.util.*;


public class MusicGraph {
    private Map<String, Song> songCatalog = new HashMap<>(); // stores Song objects
    private Map<String, List<String>> genres = new HashMap<>();
    private Map<String, List<String>> artists = new HashMap<>();
    
    public void addSong(String songTitle, List<String> songGenres, List<String> songArtists) {
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
        addSong("dusk", List.of("R&B", "Soul"), List.of("Sonny Zero"));
        addSong("forgot the time", List.of("R&B", "Soul"), List.of("Sonny Zero"));
        addSong("Sabotage", List.of("R&B", "Soul"), List.of("Sonny Zero"));
        addSong("oxygen", List.of("R&B"), List.of("Sonny Zero"));
        addSong("nosebleed", List.of("R&B", "Soul"), List.of("Sonny Zero"));

        addSong("baby", List.of("Pop Rap"), List.of("Justin Bieber"));
        addSong("Love yourself", List.of("Pop"), List.of("Justin Bieber"));
        addSong("beauty and a beat", List.of("Electropop"), List.of("Justin Bieber"));
        addSong("holy", List.of("Pop"), List.of("Justin Bieber"));
        addSong("ghost", List.of("Pop", "Rock"), List.of("Justin Bieber"));

        addSong("Billie jean", List.of("R&B"), List.of("Michael Jackson"));
        addSong("Beat it", List.of("Rock"), List.of("Michael Jackson"));
        addSong("Smooth criminal", List.of("Pop"), List.of("Michael Jackson"));
        addSong("thriller", List.of("Disco-funk"), List.of("Michael Jackson"));
        addSong("Man in the mirror", List.of("Pop"), List.of("Michael Jackson"));

        addSong("whyyy", List.of("Indie Pop"), List.of("bixby"));
        addSong("Take Time", List.of("Indie Pop"), List.of("Joon", "bixby"));
        addSong("distance", List.of("Alternative Pop"), List.of("bixby"));
        addSong("easy", List.of("Alternative Pop", "Indie Pop"), List.of("bixby"));
        addSong("endlessly", List.of("Alternative Pop", "Indie Pop"), List.of("bixby"));
        addSong("Clarity", List.of("Indie Pop", "Electronic"), List.of("Tom Frane"));
        addSong("pretty", List.of("Classical"), List.of("JVKE"));
        addSong("golden hour", List.of("Classical"), List.of("JVKE"));
        addSong("A Thousand Years", List.of("Classical"), List.of("John Michael Howell", "JVKE", "ZVC"));
        addSong("by my side", List.of("Pop"), List.of("Joon", "bixby"));

        addSong("Never Gonna Give You Up", List.of("Electronic", "Electro-Pop"), List.of("Rick Astley"));

        addSong("Goosebumps", List.of("Trap", "Psychedelic Rap"), List.of("Travis Scott"));
        addSong("Sicko Mode", List.of("Trap", "Psychedelic Rap"), List.of("Travis Scott"));
        addSong("Fe!n", List.of("Trap", "Rage Rap"), List.of("Travis Scott"));

        addSong("Sweater Weather", List.of("Indie"), List.of("The neighbourhood"));
        addSong("Daddy Issues", List.of("Indie"), List.of("The neighbourhood"));
        addSong("Softcore", List.of("Indie"), List.of("The neighbourhood"));

        addSong("Doja", List.of("UK Drill"), List.of("Central Cee"));
        addSong("Sprinter", List.of("UK Drill"), List.of("Central Cee"));
        addSong("Band4Band", List.of("UK Drill"), List.of("Central Cee"));

        addSong("Dakiti", List.of("Latin Trap"), List.of("Bad Bunny"));
        addSong("Titi Me Pregunto", List.of("Latin Trap"), List.of("Bad Bunny"));
        addSong("Mia", List.of("Latin Trap"), List.of("Bad Bunny"));

        addSong("Water", List.of("Afrobeats"), List.of("Tyla"));
        addSong("Truth or Dare", List.of("Afrobeats"), List.of("Tyla"));
        addSong("Jump", List.of("Afrobeats"), List.of("Tyla"));

        addSong("telepatia", List.of("Neo-Soul"), List.of("Kali Uchis"));
        addSong("moonlight", List.of("Neo-Soul"), List.of("Kali Uchis"));
        addSong("Never be yours", List.of("Neo-Soul"), List.of("Kali Uchis"));

        addSong("Back In Black", List.of("Heavy Metal"), List.of("AC/DC"));
        addSong("Highway to Hell", List.of("Heavy Metal"), List.of("AC/DC"));
        addSong("Thunderstruck", List.of("Heavy Metal"), List.of("AC/DC"));

        addSong("Hotel California", List.of("Country Rock"), List.of("The Eagles"));
        addSong("Take it Easy", List.of("Country Rock"), List.of("The Eagles"));
        addSong("Desperado", List.of("Country Rock"), List.of("The Eagles"));
    }

    public static void main(String[] args) {
        MusicGraph graph = new MusicGraph();
        graph.loadDataSet();
    }
}
