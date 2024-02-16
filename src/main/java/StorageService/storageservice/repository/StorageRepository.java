package StorageService.storageservice.repository;

import StorageService.storageservice.modal.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<FileData,Long> {

    Optional<FileData>findByName(String fileName);
}
