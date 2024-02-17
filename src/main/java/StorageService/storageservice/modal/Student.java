package StorageService.storageservice.modal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "excel_student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Long;
    private String name;
    private String age;
    private String email;
    private String university;

}
