// 代码生成时间: 2025-08-15 03:59:35
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * DocumentConverter is a Javalin-based REST API application providing document conversion services.
 * It currently supports converting Word documents to plain text.
 */
public class DocumentConverter {

    private static final String WORD_TO_TEXT_ENDPOINT = "/convert/word-to-text";
    private static final String CONTENT_TYPE_WORD = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    private static final String CONTENT_TYPE_TEXT = "text/plain";

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.routes(() -> {
            post(WORD_TO_TEXT_ENDPOINT, DocumentConverter::handleWordToText);
        });
    }

    /**
     * Handles the HTTP POST request to convert a Word document to plain text.
     * @param ctx The Javalin HTTP context.
     */
    private static void handleWordToText(Context ctx) {
        String documentPath = ctx.body();
        try {
            // Read the Word document
            XWPFDocument document = new XWPFDocument(new FileInputStream(documentPath));
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            StringBuilder textBuilder = new StringBuilder();

            // Extract text from paragraphs
            for (XWPFParagraph paragraph : paragraphs) {
                textBuilder.append(paragraph.getText()).append("
");
            }

            // Write the extracted text to a plain text file
            String outputPath = documentPath.replace(".docx", ".txt");
            Files.write(Paths.get(outputPath), textBuilder.toString().getBytes());

            // Return the path of the converted text file
            ctx.status(200).result(outputPath);
        } catch (IOException e) {
            // Handle I/O errors
            ctx.status(500).result("Error converting document: " + e.getMessage());
        }
    }
}