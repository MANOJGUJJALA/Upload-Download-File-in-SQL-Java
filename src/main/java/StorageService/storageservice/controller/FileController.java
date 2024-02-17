package StorageService.storageservice.controller;

import StorageService.storageservice.modal.FileData;
import StorageService.storageservice.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
@Profile(value = {"uat","prod","local"})
public class FileController {

    @Autowired
    private  StorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<String > uploadFile(@RequestParam(name = "file")MultipartFile multipartFile) throws IOException {

       String fileuploaded= storageService.uploadFile(multipartFile);

        return ResponseEntity.status(HttpStatus.OK).body(fileuploaded);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable(name = "fileName")String fileNamee){
       byte[] fileData=  storageService.downloadFile(fileNamee);

       return  ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(fileData);

    }

    @PostMapping("/excel")

    public ResponseEntity<String > excelUpload(@RequestParam(name = "excel")MultipartFile multipartFile) throws IOException {

       String response= storageService.excelUpload(multipartFile);
        return new ResponseEntity<>(response,HttpStatus.OK);

    }


}
