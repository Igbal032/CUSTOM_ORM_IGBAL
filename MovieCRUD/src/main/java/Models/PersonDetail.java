package Models;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "people_details")
public class PersonDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personDetail_id_generator")
    @SequenceGenerator(name="personDetail_id_generator", allocationSize = 1,initialValue = 1)
    private long id;

    private String about;

    private String phoneNumber;

    @OneToOne(mappedBy = "personDetail", fetch = FetchType.LAZY)
    private Person person;

}
