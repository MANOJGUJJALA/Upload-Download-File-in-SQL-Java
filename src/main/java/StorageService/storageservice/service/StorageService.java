package StorageService.storageservice.service;

import StorageService.storageservice.FileUtils.FileUtils;
import StorageService.storageservice.modal.FileData;
import StorageService.storageservice.repository.StorageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class StorageService {

    @Autowired
    private StorageRepository storageRepository;

    public String uploadFile(MultipartFile multipartFile) throws IOException {
       Optional<FileData>fileData = Optional.of(storageRepository.save(FileData.builder()
               .name(multipartFile.getOriginalFilename())
               .type(multipartFile.getContentType())
               .fileData(FileUtils.compressFile(multipartFile.getBytes()))
               .build()));

       if(fileData.isEmpty()){
           return  null;
       }
       FileData fileData1=fileData.get();

       return "File Uploaded Succesfully "+ multipartFile.getOriginalFilename();

    }

    @Transactional

    public byte[] downloadFile(String name){
        System.out.println(" name is "+name);
       Optional<FileData>dbfileData= storageRepository.findByName(name);

       byte[] requiredFileData=FileUtils.decompressFile(dbfileData.get().getFileData());

       return requiredFileData;
    }
}
