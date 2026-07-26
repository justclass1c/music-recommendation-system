import java.util.List;
import java.util.ArrayList;

public class Song {
    private String songTitle;
    private List<String> songGenres;
    private List<String> songArtists;

    public Song() {};

    public Song(String songTitle, List<String> songGenres, List<String> songArtists) {
        this.songTitle = songTitle;
        this.songGenres = songGenres;
        this.songArtists = songArtists;
    }

    public String getSongTitle() {
        return this.songTitle;
    }

    public List<String> getSongGenres() {
        return this.songGenres;
    }

    public List<String> getSongArtists() {
        return this.songArtists;
    }

}
