package com.explore.boot.wkhtml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jillukowicz
 */
@RestController
public class WkhtmlController {

  @Autowired
  private WkhtmlPdfCreator pdfCreator;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public void home(HttpServletResponse response) throws IOException {
    response.getWriter().print("Hi!");
  }


  @RequestMapping(value = "/api/pdf", method = RequestMethod.GET)
  public void pdf(HttpServletResponse response) {
    /*@RequestBody WkhtmlRequest request, */
    WkhtmlRequest request = new WkhtmlRequest();
    request.setIn("https://code-complete.herokuapp.com/cv-preview");
    pdfCreator.create(request, response);
  }
}
