package com.example.utro.web;

import com.example.utro.service.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/pdf")
@CrossOrigin
public class PDFController {

    @Autowired
    private PDFService pdfService;

    @GetMapping("/generate/remains")
    public void generatePDFAboutRemains(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setCharacterEncoding("UTF-16LE");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_remains_" + currentDateTime + ".pdf; charset=UTF-16LE";
        response.setHeader(headerKey, headerValue);
        this.pdfService.remains(response);
    }
    @GetMapping("/generate/moves")
    public void generatePDFAboutMoves(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setCharacterEncoding("UTF-16LE");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_moves_" + currentDateTime + ".pdf; charset=UTF-16LE";
        response.setHeader(headerKey, headerValue);
        this.pdfService.moves(response);
    }
}