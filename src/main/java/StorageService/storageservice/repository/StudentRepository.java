package StorageService.storageservice.repository;

import StorageService.storageservice.modal.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {

}
