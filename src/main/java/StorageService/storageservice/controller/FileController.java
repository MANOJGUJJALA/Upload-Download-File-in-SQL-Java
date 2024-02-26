package StorageService.storageservice.controller;

import StorageService.storageservice.dto.Studentdto;
import StorageService.storageservice.modal.FileData;
import StorageService.storageservice.service.StorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/upload")
    public ResponseEntity<String > uploadFile(@RequestParam(name = "file")MultipartFile multipartFile) throws IOException {

       String fileuploaded= storageService.uploadFile(multipartFile);

        return ResponseEntity.status(HttpStatus.OK).body(fileuploaded);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable(name = "fileName")String fileNamee){
        System.out.println("calling ----");
       byte[] fileData=  storageService.downloadFile(fileNamee);

        System.out.println("filedata in byte is "+ fileData);

       return  ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(fileData);

    }

    @PostMapping("/excel")

    public ResponseEntity<String > excelUpload( @RequestParam ("studentDtoJson")String studentDtoJson,@RequestParam(name = "excel")MultipartFile multipartFile) throws IOException {

        System.out.println("came in controller"+studentDtoJson);
        Studentdto studentdto=null;

        try{

         studentdto=objectMapper.readValue(studentDtoJson,Studentdto.class);
            System.out.println("processed is "+studentdto);
        }
        catch (JsonProcessingException e){
            throw new RuntimeException();
        }
       String response= storageService.excelUpload(multipartFile,studentdto);
        return new ResponseEntity<>(response,HttpStatus.OK);

    }


}
