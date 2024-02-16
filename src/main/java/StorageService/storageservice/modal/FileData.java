package StorageService.storageservice.modal;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
//@Table(name = "FileData")
@Table(name = "filedata")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FileData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

    private String name;
    private String type;
    @Lob
    @Column(name = "file_data",length = 1000)


    private byte[] fileData;
}
