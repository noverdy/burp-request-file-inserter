package com.yesver.burpfileinserter.actions;

import burp.api.montoya.core.ByteArray;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.MessageEditorHttpRequestResponse;
import com.yesver.burpfileinserter.ExtensionContext;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

public class FileUploadHandler implements ActionListener {
    private final ExtensionContext ctx;
    private final ContextMenuEvent event;
    private final Function<byte[], byte[]> dataProcessor;

    public FileUploadHandler(ExtensionContext ctx, ContextMenuEvent event) {
        this.ctx = ctx;
        this.event = event;
        this.dataProcessor = Function.identity();
    }

    public FileUploadHandler(ExtensionContext ctx, ContextMenuEvent event, Function<byte[], byte[]> dataProcessor) {
        this.ctx = ctx;
        this.event = event;
        this.dataProcessor = dataProcessor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ctx.logger().info("File upload action triggered.");

        Optional<MessageEditorHttpRequestResponse> editorOpt = event.messageEditorRequestResponse();
        if (editorOpt.isEmpty()) {
            ctx.logger().error("Editor not found.");
            return;
        }

        MessageEditorHttpRequestResponse editor = editorOpt.get();

        JFileChooser fileChooser = new JFileChooser();
        int choice = fileChooser.showOpenDialog(null);
        if (choice != JFileChooser.APPROVE_OPTION) {
            ctx.logger().info("File chooser cancelled by user.");
            return;
        }

        Path filePath = fileChooser.getSelectedFile().toPath();
        ctx.logger().info("User selected file: %s", filePath);

        byte[] fileBytes;
        try {
            fileBytes = Files.readAllBytes(filePath);
        } catch (IOException ex) {
            ctx.logger().error("Failed to read file: %s", ex.getMessage());
            return;
        }

        byte[] processedBytes = dataProcessor.apply(fileBytes);

        int caretPosition = editor.caretPosition();
        HttpRequest originalRequest = editor.requestResponse().request();
        ByteArray originalBytes = originalRequest.toByteArray();

        if (caretPosition < 0 || caretPosition > originalBytes.length()) {
            ctx.logger().error("Caret position out of bounds: %d (request length: %d)", caretPosition, originalBytes.length());
            return;
        }

        ByteArray newRequestBytes = insertBytesAt(originalBytes, processedBytes, caretPosition);

        HttpRequest newRequest = HttpRequest.httpRequest(newRequestBytes);
        editor.setRequest(newRequest);

        ctx.logger().info("Inserted %d bytes at position %d. Request updated successfully.\n", processedBytes.length, caretPosition);
    }

    private ByteArray insertBytesAt(ByteArray original, byte[] bytesToInsert, int position) {
        ByteArray before = original.subArray(0, position);
        ByteArray after = original.subArray(position, original.length());
        return before.withAppended(bytesToInsert).withAppended(after);
    }
}
