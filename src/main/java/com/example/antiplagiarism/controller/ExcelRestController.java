package com.example.antiplagiarism.controller;

import com.example.antiplagiarism.service.common.ExcelService;
import com.example.antiplagiarism.service.database.TextTestService;
import com.example.antiplagiarism.service.model.TextTestDto;
import com.example.antiplagiarism.service.model.TextTestSubmitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@RestController
@RequiredArgsConstructor
public class ExcelRestController {

    private final ExcelService excelService;
    private final TextTestService textTestService;

    @PostMapping("/report")
    public ResponseEntity<byte[]> doDownloadReport(@RequestBody TextTestSubmitDto textTestSubmitDto) {
        return buildReportResponse(null, textTestSubmitDto);
    }

    @PostMapping("/{id}/report")
    public ResponseEntity<byte[]> doDownloadReport(@PathVariable Long id) {
        TextTestDto textTestDto = textTestService.findById(id);
        TextTestSubmitDto textTestSubmitDto = new TextTestSubmitDto(textTestDto.getTextOne(),
                textTestDto.getTextTwo());
        return buildReportResponse(textTestDto.getId(), textTestSubmitDto);
    }

    private ResponseEntity<byte[]> buildReportResponse(Long id, TextTestSubmitDto textTestSubmitDto) {
        ByteArrayResource resource = new ByteArrayResource(excelService.createTextTestReport(id, textTestSubmitDto));
        HttpHeaders headers = new HttpHeaders();
        headers.set(CONTENT_TYPE, "application/vnd.ms-excel");
        headers.setContentDispositionFormData("attachment", "report.xls");
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource.getByteArray());
    }

}
