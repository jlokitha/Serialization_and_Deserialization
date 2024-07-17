package lk.ijse.serialization_and_deserialization.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

@WebServlet("/deserialize")
@MultipartConfig
public class DeserializationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Get byte array string from request body
        String base64File = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);

        if (!base64File.isEmpty()) {

            // Decode base64 string to byte array
            byte[] fileBytes = Base64.getDecoder().decode(base64File);

            // Set content type and file name in response header
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"uploaded-file.pdf\"");

            // Process the byte array and write it in outputStream
            try (InputStream inputStream = new ByteArrayInputStream(fileBytes);
                 OutputStream outputStream = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } else {
            // Send error if no file is received
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty or invalid Base64 string");
        }
    }
}
