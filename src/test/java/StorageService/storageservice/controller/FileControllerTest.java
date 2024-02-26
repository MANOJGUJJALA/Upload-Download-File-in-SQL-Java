package StorageService.storageservice.controller;


import StorageService.storageservice.dto.Studentdto;
import StorageService.storageservice.service.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileController.class)

public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private  StorageService storageService;

    @Autowired
    private  ObjectMapper objectMapper;
    @Test
    void downloadFileTest() throws Exception{
        byte[]k=new byte[2];
        k[0]=-1;
        k[1]=-40;

        Mockito.when(storageService.downloadFile("pan.jpg")).thenReturn(k);

        mockMvc.perform(get("/files/download/pan.jpg"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_PNG_VALUE))
                .andExpect(MockMvcResultMatchers.content().bytes(k));
    }

    @Test
    void uploadFileTest () throws Exception{
//        MockMultipartFile multipartFile=Mockito.mock(MockMultipartFile.class);
     MockMultipartFile multipartFile = new MockMultipartFile("file", "pan.jpg", "image/png", "Test file content".getBytes());


        Mockito.when(storageService.uploadFile(multipartFile)).thenReturn("File Uploaded Succesfully");

        mockMvc.perform(MockMvcRequestBuilders.multipart("/files/upload")
                .file( multipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))


            .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("File Uploaded Succesfully"));
    }

    @Test
    void uploadExcelTest()throws Exception{
        MockMultipartFile multipartFile=new MockMultipartFile("excel","excel.xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","".getBytes());

        Studentdto studentdto = new Studentdto();
        studentdto.setName("studentdto");
        studentdto.setAge("50");
        studentdto.setEmail("email@email.com");
        studentdto.setUniversity("studentDtoUni");

        String studentDtoJson = objectMapper.writeValueAsString(studentdto);

        Mockito.when(storageService.excelUpload(multipartFile,studentdto)).thenReturn("Excel Sheet Succesfully Uploaded");

         mockMvc.perform(MockMvcRequestBuilders.multipart("/files/excel")
                        .file( multipartFile)
                        .param("studentDtoJson", studentDtoJson)
                        .contentType(MediaType.MULTIPART_FORM_DATA))


                .andExpect(MockMvcResultMatchers.status().isOk());
//                        .andExpect(MockMvcResultMatchers.content().string("Excel Sheet Succesfully Uploaded"));
        System.out.println("obtained is --"+MockMvcResultMatchers.content());
//        resultActions.andExpect(MockMvcResultMatchers.content().string("Excel Sheet Succesfully Uploaded"));


//        assertEquals(MockMvcResultMatchers.content().string("Excel Sheet Succesfully Uploaded"),null);

    }


}
