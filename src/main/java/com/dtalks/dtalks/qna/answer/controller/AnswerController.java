package com.dtalks.dtalks.qna.answer.controller;

import com.dtalks.dtalks.qna.answer.dto.AnswerDto;
import com.dtalks.dtalks.qna.answer.dto.AnswerResponseDto;
import com.dtalks.dtalks.qna.answer.service.AnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions/{questionId}/answers")
public class AnswerController {
    private final AnswerService answerService;

    @GetMapping
    public ResponseEntity<List<AnswerResponseDto>> getAnswersByQuestionId(@PathVariable Long questionId){
        return ResponseEntity.ok(answerService.getAnswersByQuestionId(questionId));
    }

    @PostMapping
    public ResponseEntity<Long> createAnswer(@Valid @RequestBody AnswerDto answerDto, @PathVariable Long questionId) {
        return ResponseEntity.ok(answerService.createAnswer(answerDto, questionId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateAnswer(@Valid @RequestBody AnswerDto answerDto, @PathVariable Long id, @PathVariable Long questionId) {
        return ResponseEntity.ok(answerService.updateAnswer(id, answerDto));
    }

    @DeleteMapping("/{id}")
    public void deleteAnswer(@PathVariable Long id, @PathVariable Long questionId) {
        answerService.deleteAnswer(id);
    }
}
