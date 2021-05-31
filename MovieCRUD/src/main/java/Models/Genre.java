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
@Table(name = "genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genre_id_generator")
    @SequenceGenerator(name="genre_id_generator", allocationSize = 1,initialValue = 1)
    private long id;

    private String name;

    @ManyToMany(mappedBy = "genres", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Movie> movies = new ArrayList<>();

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "deleted_date")
    private LocalDate deletedDate;
}
