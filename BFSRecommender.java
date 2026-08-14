import java.util.*;

public class BFSRecommender {

    public List<String> recommend(MusicGraph graph, String startTitle, int maxHops) {
        Map<String, Song> songCatalog = graph.getSongCatalog();
        Map<String, List<String>> genres = graph.getGenres();
        Map<String, List<String>> artists = graph.getArtists();

        if (!songCatalog.containsKey(startTitle)) {
            System.out.println("Error: Song \"" + startTitle + "\" not found.");
            return new ArrayList<>();
        }

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, Integer> distances = new HashMap<>();
        List<String> results = new ArrayList<>();

        queue.offer(startTitle);
        visited.add(startTitle);
        distances.put(startTitle, 0);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int currentDist = distances.get(current);

            if (currentDist > maxHops) break;

            // If current is a song (and not the start), add to results
            if (songCatalog.containsKey(current) && !current.equals(startTitle)) {
                results.add(current);
            }

            // Determine neighbors based on node type
            List<String> neighbors = new ArrayList<>();
            if (songCatalog.containsKey(current)) {
                Song song = songCatalog.get(current);
                neighbors.addAll(song.getSongGenres());
                neighbors.addAll(song.getSongArtists());
            } else if (genres.containsKey(current)) {
                neighbors.addAll(genres.get(current));
            } else if (artists.containsKey(current)) {
                neighbors.addAll(artists.get(current));
            }

            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                    distances.put(neighbor, currentDist + 1);
                }
            }
        }

        return results;
    }
}