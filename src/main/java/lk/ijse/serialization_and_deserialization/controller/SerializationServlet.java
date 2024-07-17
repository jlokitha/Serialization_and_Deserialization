package lk.ijse.serialization_and_deserialization.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.*;
import java.util.Base64;

@WebServlet("/serialize")
@MultipartConfig
public class SerializationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Get uploaded file form request
        Part filePart = request.getPart("file");

        if (filePart != null) {

            byte[] fileBytes;

            // Convert the file into byte array
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                filePart.getInputStream().transferTo(baos);
                fileBytes = baos.toByteArray();
            }

            // Encode byte array into base64 string
            String base64File = Base64.getEncoder().encodeToString(fileBytes);

            // Set content type in response header
            response.setContentType("text/plain");

            // Return string as the response
            response.getWriter().write(base64File);
        } else {
            // Send error if no file is received
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No file uploaded");
        }
    }
}
