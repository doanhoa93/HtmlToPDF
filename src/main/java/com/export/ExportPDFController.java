package com.export;

import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class ExportPDFController {

    private final PdfCreator pdfCreator;

    @Autowired
    public ExportPDFController(PdfCreator pdfCreator) {
        this.pdfCreator = pdfCreator;
    }

    @GetMapping(value = "/preview")
    public void preview(HttpServletResponse response) {
        PdfFileRequest request = new PdfFileRequest();
        request.setSourceHtmlUrl("https://code-complete.herokuapp.com/cv-preview");
        pdfCreator.create(request, response);
    }

    @PostMapping("/export-pdf")
    public void export(@RequestBody PdfFileRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(request.getSourceHtmlUrl())) {
            request.setSourceHtmlUrl("https://code-complete.herokuapp.com/cv-preview");
        }
        pdfCreator.create(request, response);
    }
}
