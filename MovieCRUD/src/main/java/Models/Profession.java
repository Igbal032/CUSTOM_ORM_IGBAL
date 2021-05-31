package Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "professions")
public class Profession {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profession_id_generator")
    @SequenceGenerator(name="profession_id_generator", allocationSize = 1,initialValue = 1)
    private long id;

    private String name;

    @OneToMany(mappedBy = "profession",cascade = {CascadeType.ALL})
    private List<Person> people;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "deleted_date")
    private LocalDate deletedDate;
}
