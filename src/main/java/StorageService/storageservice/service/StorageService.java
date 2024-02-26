package StorageService.storageservice.service;

import StorageService.storageservice.FileUtils.FileUtils;
import StorageService.storageservice.modal.FileData;
import StorageService.storageservice.modal.Student;
import StorageService.storageservice.repository.StorageRepository;
import StorageService.storageservice.repository.StudentRepository;
import jakarta.transaction.Transactional;
//import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StorageService {

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private StudentRepository studentRepository;

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

//        System.out.println("obtaoined is "+ dbfileData);

       byte[] requiredFileData=FileUtils.decompressFile(dbfileData.get().getFileData());

        System.out.println("required filedata is "+requiredFileData);

       return requiredFileData;
    }

    @Transactional
    public String  excelUpload(MultipartFile multipartFile) throws IOException {

        System.out.println("file name "+multipartFile.getOriginalFilename());

        List<List<String>> rows = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(multipartFile.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        // Iterator<Row> rowIterator = sheet.iterator();
        rows = StreamSupport.stream(sheet.spliterator(), false)
                .map(row -> StreamSupport
                        .stream(row.spliterator(), false)
                        .map(this::getCellStringValue)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
        System.out.println("rows :: " + rows);
        // Save data to the database
        List<Student> excelDataList = rows.stream().map(row -> {
            Student excelData = new Student();
            excelData.setName(row.get(0));
            excelData.setAge(row.get(1));
            excelData.setEmail(row.get(2));
            excelData.setUniversity(row.get(3));
            return excelData;
        }).collect(Collectors.toList());
        studentRepository.saveAll(excelDataList);

      return "Excel Sheet"+  multipartFile.getOriginalFilename()+" Succesfully Uploaded";
    }

    private String getCellStringValue(Cell cell) {
        CellType cellType = cell.getCellType();

        if (cellType == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cellType == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else if (cellType == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        }

        return null;
    }
}
