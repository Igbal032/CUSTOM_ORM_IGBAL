package Models;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_id_generator")
    @SequenceGenerator(name="movie_id_generator", allocationSize = 1,initialValue = 1)
    private long id;

    private String title;

    @OneToMany(mappedBy = "movie",cascade = {CascadeType.ALL})
    private List<Person> people;

//    @OneToMany(mappedBy = "movie",cascade = {CascadeType.ALL})


    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "movie_to_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )    private List<Genre> genres = new ArrayList<>();

    @Column(name = "published_date")
    private LocalDate publishedDate;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "deleted_date")
    private LocalDate deletedDate;
}
