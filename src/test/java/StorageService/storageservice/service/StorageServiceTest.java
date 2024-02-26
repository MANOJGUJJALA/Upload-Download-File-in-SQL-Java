package StorageService.storageservice.service;

import StorageService.storageservice.FileUtils.FileUtils;
import StorageService.storageservice.modal.FileData;
import StorageService.storageservice.modal.Student;
import StorageService.storageservice.repository.StorageRepository;
import StorageService.storageservice.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;


@ExtendWith(MockitoExtension.class)
public class StorageServiceTest {

    @InjectMocks
    private StorageService storageService;

    @Mock
    private StorageRepository storageRepository;

    @Mock
    private StudentRepository studentRepository;

    @Test
    void uploadFile_shouldReturnSuccessMessage() throws IOException {
        // Mocking MultipartFile
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("testFile.txt");
        when(multipartFile.getContentType()).thenReturn("text/plain");
        when(multipartFile.getBytes()).thenReturn("Test file content".getBytes());

        // Mocking StorageRepository
        FileData mockedFileData = new FileData();
        when(storageRepository.save(any(FileData.class))).thenReturn(mockedFileData);

        // Call the method
        String result = storageService.uploadFile(multipartFile);

        // Assertions
        assertNotNull(result);
        assertEquals("File Uploaded Successfully testFile.txt", result);
    }

    @Test
    void uploadFileReturingNullWhenFileDataIsEmpty() throws IOException{

        MultipartFile multipartFile=mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("testFile.txt");
        when(multipartFile.getContentType()).thenReturn("text/plain");
        when(multipartFile.getBytes()).thenReturn("Test file content".getBytes());

        when(storageRepository.save(any(FileData.class))).thenReturn(null);

        String result=storageService.uploadFile(multipartFile);
        assertNull(result);
        assertEquals(null,result);
    }


    @Test
    @Transactional
    void downloadFileTest() throws Exception {
        // Arrange
        String testName = "testFileName";
        byte[] testFileData = "Test file content".getBytes();
        FileData fileData = new FileData();
        fileData.setName(testName);
        fileData.setFileData(FileUtils.compressFile(testFileData));

        when(storageRepository.findByName(testName)).thenReturn(Optional.of(fileData));

        // Act
        byte[] result = storageService.downloadFile(testName);

        // Assert
        assertNotNull(result);
        assertArrayEquals(testFileData, result);
    }


    @Test
    @Transactional
    void excelUploadTest() throws Exception {
        String fileName = "excel.xlsx";
        String filePath = "C:\\Users\\balu8\\Desktop\\" + fileName;  // Use the absolute path
        InputStream inputStream = new FileInputStream(filePath);
        MockMultipartFile file = new MockMultipartFile("file", fileName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", inputStream);


        // Act
        String result = storageService.excelUpload(file);

        // Assert
//        System.out.println("result is "+result);
        assertNotNull(result);
        assertTrue(result.contains("Successfully Uploaded"));

//        Workbook workbook = WorkbookFactory.create(file.getInputStream());
//        Sheet sheet = workbook.getSheetAt(0);
//        Row row = sheet.createRow(0);
//        Cell cell = row.createCell(0);
//        cell.setCellValue(true);  // Set a boolean value in the sheet
//        String booleanCellValue = storageService.getCellStringValue(cell);
//        assertEquals("true", booleanCellValue);
    }

}
