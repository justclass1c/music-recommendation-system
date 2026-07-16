import java.util.List;
import java.util.ArrayList;

public class Song {
    private String title;
    private List<String> genres;
    private List<String> artists;

    public Song() {};

    public Song(String title, List<String> genres, List<String> artists) {
        this.title = title;
        this.genres = genres;
        this.artists = artists;
    }

    public String getTitle() {
        return this.title;
    }

    public List<String> getGenres() {
	return this.genres;
    }

    public List<String> getArtists() {
	return this.artists;
    }
}
