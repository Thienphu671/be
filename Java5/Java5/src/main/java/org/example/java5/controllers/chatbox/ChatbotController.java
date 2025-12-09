package org.example.java5.controllers.chatbox;


import org.example.java5.fashionbot.AIModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> askChatbot(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        String answer = generateAnswer(question); // Xử lý logic trả lời
        Map<String, String> response = new HashMap<>();
        response.put("answer", answer);
        return ResponseEntity.ok(response);
    }

    private String generateAnswer(String question) {
        if (question.contains("mua quần áo")) {
            return "Bạn có thể xem các sản phẩm tại trang chủ.";
        }
        return "Xin lỗi, tôi chưa hiểu câu hỏi của bạn.";
    }
}
