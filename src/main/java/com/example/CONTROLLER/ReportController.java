package com.example.CONTROLLER;

import com.example.SERVICE.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/inventory")
    @PreAuthorize("hasAnyRole('INVITED', 'ADMIN')")
    public Map<String, Object> getInventoryReport() {
        return reportService.generateInventoryReport();
    }
}