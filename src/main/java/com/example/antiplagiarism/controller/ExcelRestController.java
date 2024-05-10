package com.example.antiplagiarism.controller;

import com.example.antiplagiarism.service.common.ExcelService;
import com.example.antiplagiarism.service.model.TextTestDto;
import com.example.antiplagiarism.service.model.TextTestSubmitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@RestController
@RequiredArgsConstructor
public class ExcelRestController {

    private final ExcelService excelService;

    @PostMapping("/report")
    public ResponseEntity<byte[]> doDownloadReport(@RequestBody TextTestSubmitDto textTestSubmitDto, HttpServletRequest request, HttpServletResponse response) {
        TextTestDto textTestDto = new TextTestDto();
        textTestDto.setTextOne(textTestSubmitDto.getTextOne());
        textTestDto.setTextTwo(textTestSubmitDto.getTextTwo());
        ByteArrayResource resource = new ByteArrayResource(excelService.createTextTestReport(textTestSubmitDto));
        HttpHeaders headers = new HttpHeaders();
        headers.set(CONTENT_TYPE, "application/vnd.ms-excel");
        headers.setContentDispositionFormData("attachment", "report.xls");
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource.getByteArray());
    }

}
