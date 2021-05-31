package Interfaces;
import Models.Movie;
import java.util.List;

public interface IMovie {
    void createMovie();
    void updateMovie();
    void deleteMovie();
    List<Movie> getAll();
    void searchWithName();
    void addGenreToMovie();
}
