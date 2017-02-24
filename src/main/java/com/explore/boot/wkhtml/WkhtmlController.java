package com.explore.boot.wkhtml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author jillukowicz
 */
@RestController
public class WkhtmlController {

  @Autowired
  private WkhtmlPdfCreator pdfCreator;

  @RequestMapping(value = "/api/pdf", method = RequestMethod.GET)
  public void pdf(/*@RequestBody WkhtmlRequest request, */HttpServletResponse response){
    WkhtmlRequest request = new WkhtmlRequest();
    request.setIn("http://www.acslworld.com");
    pdfCreator.create(request, response);
  }
}
