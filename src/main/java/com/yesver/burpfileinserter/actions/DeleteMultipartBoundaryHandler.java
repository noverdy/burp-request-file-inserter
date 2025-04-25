package com.yesver.burpfileinserter.actions;

import burp.api.montoya.core.ByteArray;
import burp.api.montoya.http.message.HttpHeader;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.ui.contextmenu.MessageEditorHttpRequestResponse;
import com.yesver.burpfileinserter.ExtensionContext;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class DeleteMultipartBoundaryHandler implements ActionListener {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String MULTIPART = "multipart/form-data;";
    private static final String BOUNDARY_KEY = "boundary=";

    private final ExtensionContext ctx;
    private final ContextMenuEvent event;

    public DeleteMultipartBoundaryHandler(ExtensionContext ctx, ContextMenuEvent event) {
        this.ctx = ctx;
        this.event = event;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ctx.logger().info("Delete multipart boundary action triggered.");

        Optional<MessageEditorHttpRequestResponse> editorOpt = event.messageEditorRequestResponse();
        if (editorOpt.isEmpty()) {
            ctx.logger().error("Editor not found.");
            return;
        }

        MessageEditorHttpRequestResponse editor = editorOpt.get();

        HttpRequest originalRequest = editor.requestResponse().request();
        ByteArray originalBytes = originalRequest.toByteArray();

        ctx.logger().info("Original request length: %d bytes.", originalBytes.length());

        HttpHeader contentTypeHeader = originalRequest.header(CONTENT_TYPE);
        if (contentTypeHeader == null) {
            ctx.logger().error("Content-Type header not found.");
            return;
        }

        String contentType = contentTypeHeader.value();
        ctx.logger().info("Content-Type: %s", contentType);

        if (!contentType.startsWith(MULTIPART)) {
            ctx.logger().error("Not a multipart/form-data request.");
            return;
        }

        ByteArray boundary = extractBoundary(contentType);
        if (boundary == null) {
            ctx.logger().error("Boundary not found in Content-Type.");
            return;
        }

        List<Integer> boundaryIndices = findBoundaryIndices(originalBytes, boundary);
        if (boundaryIndices.isEmpty()) {
            ctx.logger().error("Boundary not found in body.");
            return;
        }

        int caretPosition = editor.caretPosition();
        Optional<BoundaryPair> pairOpt = findBoundaryPair(boundaryIndices, caretPosition);
        if (pairOpt.isEmpty()) {
            ctx.logger().error("Caret is not between two boundaries.");
            return;
        }
        BoundaryPair pair = pairOpt.get();

        ByteArray part = originalBytes.subArray(pair.start(), pair.end());
        int headerEnd = findHeaderEnd(part);
        if (headerEnd == -1) {
            ctx.logger().error("Malformed request body: headers not found.");
            return;
        }

        ByteArray before = originalBytes.subArray(0, pair.start() + headerEnd);
        ByteArray after = originalBytes.subArray(pair.end(), originalBytes.length());

        ByteArray newRequestBytes = before.withAppended("\r\n\r\n").withAppended(after);
        HttpRequest newRequest = HttpRequest.httpRequest(newRequestBytes);
        editor.setRequest(newRequest);

        ctx.logger().info("Removed data between boundaries at %d and %d. New request length: %d bytes.\n", pair.start(), pair.end(), newRequestBytes.length());
    }

    private ByteArray extractBoundary(String contentType) {
        int idx = contentType.indexOf(BOUNDARY_KEY);
        if (idx == -1) {
            return null;
        }

        String boundary = "--" + contentType.substring(idx + BOUNDARY_KEY.length());
        return ByteArray.byteArray(boundary);
    }

    private List<Integer> findBoundaryIndices(ByteArray data, ByteArray boundary) {
        return IntStream.range(0, data.length() - boundary.length())
                .filter(i -> boundary.equals(data.subArray(i, i + boundary.length())))
                .boxed()
                .toList();
    }

    private Optional<BoundaryPair> findBoundaryPair(List<Integer> indices, int caret) {
        for (int i = 0; i < indices.size() - 1; i++) {
            int start = indices.get(i);
            int end = indices.get(i + 1);
            if (caret > start && caret < end) {
                return Optional.of(new BoundaryPair(start, end));
            }
        }
        return Optional.empty();
    }

    private int findHeaderEnd(ByteArray part) {
        String partStr = part.toString();
        int headerEnd = partStr.indexOf("\r\n\r\n");
        if (headerEnd != -1) return headerEnd + 4;
        headerEnd = partStr.indexOf("\n\n");
        if (headerEnd != -1) return headerEnd + 2;
        return -1;
    }

    private record BoundaryPair(int start, int end) {}
}
