package Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "people")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_id_generator")
    @SequenceGenerator(name="person_id_generator", allocationSize = 1,initialValue = 1)
    private long id;

    private String name;

    private String surname;

    @ManyToOne(cascade = CascadeType.ALL)
    private Movie movie;

    @ManyToOne(cascade = CascadeType.ALL)
    private Profession profession;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "person_detail_id", referencedColumnName = "id")
    private PersonDetail personDetail;

    @Column(name = "birth_date")
    private LocalDate birthDay;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "deleted_date")
    private LocalDate deletedDate;
}
