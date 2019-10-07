package com.edu.EvaluationWeb.controller;

import com.edu.EvaluationWeb.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/storage")
public class StorageController {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition";

    public final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/download/{id}")
    @ResponseBody
    public ResponseEntity<byte []> downloadFile(@PathVariable Long id) {
        Map<String, Object> data = storageService.download(id);
        return ResponseEntity.ok()
                .header(CONTENT_TYPE_HEADER, (String) data.get(StorageService.CONTENT_TYPE_KEY))
                .header(CONTENT_DISPOSITION_HEADER, "attachment; filename=" + data.get(StorageService.FILENAME_KEY))
                .body((byte[]) data.get(StorageService.DATA_KEY));
    }

}
