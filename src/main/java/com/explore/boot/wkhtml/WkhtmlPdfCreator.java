package com.explore.boot.wkhtml;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author jillukowicz
 */
@Service
public class WkhtmlPdfCreator {

  private static final Logger LOG = LoggerFactory.getLogger(WkhtmlPdfCreator.class);

  public void create(WkhtmlRequest request, HttpServletResponse response) {
    List<String> pdfCommand = Arrays.asList(
        "wkhtmltopdf",
        request.getIn(),
        "-"
    );

    ProcessBuilder pb = new ProcessBuilder(pdfCommand);
    Process pdfProcess;

    try {
      pdfProcess = pb.start();

      try (InputStream in = pdfProcess.getInputStream()) {
        pdfToResponse(in, response);
        waitForProcessInCurrentThread(pdfProcess);
        requireSuccessfulExitStatus(pdfProcess);
        setResponseHeaders(response, request);

        LOG.info("Wrote PDF file to the response from request: {}", request);
      } catch (Exception ex) {
        writeErrorMessageToLog(ex, pdfProcess);
        throw new RuntimeException("PDF generation failed");
      } finally {
        pdfProcess.destroy();
      }
    } catch (IOException ex) {
      LOG.error("Could not create a PDF file because of an error occurred: ", ex);
      throw new RuntimeException("PDF generation failed");
    }
  }

  private void pdfToResponse(InputStream in, HttpServletResponse response) throws IOException {
    LOG.debug("Writing created PDF file to HTTP response");

    OutputStream out = response.getOutputStream();
    IOUtils.copy(in, out);
    out.flush();
  }

  private void waitForProcessInCurrentThread(Process process) {
    try {
      process.waitFor(5, TimeUnit.SECONDS);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
    }

    LOG.debug("Wkhtmltopdf ended");
  }

  private void requireSuccessfulExitStatus(Process process) {
    if (process.exitValue() != 0) {
      throw new RuntimeException("PDF generation failed");
    }
  }

  private void setResponseHeaders(HttpServletResponse response, WkhtmlRequest request) {
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=\"" + request.getOut() + "\"");
  }

  private void writeErrorMessageToLog(Exception ex, Process pdfProcess) throws IOException {
    LOG.error("Could not create PDF because an exception was thrown: ", ex);
    LOG.error("The exit value of PDF process is: {}", pdfProcess.exitValue());

    String errorMessage = getErrorMessageFromProcess(pdfProcess);
    LOG.error("PDF process ended with error message: {}", errorMessage);
  }

  private String getErrorMessageFromProcess(Process pdfProcess) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(pdfProcess.getErrorStream()));
      StringWriter writer = new StringWriter();

      String line;
      while ((line = reader.readLine()) != null) {
        writer.append(line);
      }

      return writer.toString();
    } catch (IOException ex) {
      LOG.error("Could not extract error message from process because an exception was thrown", ex);
      return "";
    }
  }
}
