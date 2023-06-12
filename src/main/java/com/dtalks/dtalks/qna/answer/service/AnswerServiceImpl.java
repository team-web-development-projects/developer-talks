package com.dtalks.dtalks.qna.answer.service;

import com.dtalks.dtalks.exception.exception.*;
import com.dtalks.dtalks.qna.answer.dto.AnswerDto;
import com.dtalks.dtalks.qna.answer.dto.AnswerResponseDto;
import com.dtalks.dtalks.qna.answer.entity.Answer;
import com.dtalks.dtalks.qna.answer.repository.AnswerRepository;
import com.dtalks.dtalks.exception.ErrorCode;
import com.dtalks.dtalks.qna.question.entity.Question;
import com.dtalks.dtalks.qna.question.repository.QuestionRepository;
import com.dtalks.dtalks.user.Util.SecurityUtil;
import com.dtalks.dtalks.user.entity.User;
import com.dtalks.dtalks.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Override
    @Transactional(readOnly = true)
    public AnswerResponseDto searchById(Long id) {
        Optional<Answer> optionalAnswer = answerRepository.findById(id);
        if (optionalAnswer.isEmpty()) {
            throw new CustomException(ErrorCode.ANSWER_NOT_FOUND_ERROR, "존재하지 않는 답변입니다. ");
        }
        Answer answer = optionalAnswer.get();
        return AnswerResponseDto.toDto(answer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnswerResponseDto> getAnswersByQuestionId(Long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isEmpty()) {
            throw new CustomException(ErrorCode.QUESTION_NOT_FOUND_ERROR, "존재하지 않는 질문입니다. ");
        }
        return answerRepository.findByQuestionId(questionId).stream()
                .map(AnswerResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnswerResponseDto> getAnswersByUserId(Long userId){
        return answerRepository.findByUserId(userId).stream()
                .map(AnswerResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Long createAnswer(AnswerDto answerDto,  Long questionId) {
        User user = SecurityUtil.getUser();
        Optional<Question> question = questionRepository.findById(questionId);
        if(question.isEmpty()){
            throw new CustomException(ErrorCode.POST_NOT_FOUND_ERROR, "해당하는 질문이 존재하지 않습니다. ");
        }
        Answer answer = Answer.toEntity(answerDto, question.get(), user);
        answerRepository.save(answer);
        return answer.getId();
    }

    @Override
    @Transactional
    public Long updateAnswer(Long answerId, AnswerDto answerDto) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isEmpty()) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND_ERROR, "해당하는 답변이 존재하지 않습니다. ");
        }
        Answer answer = optionalAnswer.get();
        String userId = answer.getUser().getUserid();
        if (!userId.equals(SecurityUtil.getUser().getUserid())) {
            throw new CustomException(ErrorCode.PERMISSION_NOT_GRANTED_ERROR, "해당 답변을 수정할 수 있는 권한이 없습니다.");
        }
        answer.update(answerDto.getContent());
        return answerId;
    }

    @Override
    @Transactional
    public void deleteAnswer(Long id) {
        Optional<Answer> optionalAnswer = answerRepository.findById(id);
        if(optionalAnswer.isEmpty()){
            throw new CustomException(ErrorCode.POST_NOT_FOUND_ERROR, "해당하는 답변이 존재하지 않습니다. ");
        }
        Answer answer = optionalAnswer.get();
        String userId = answer.getUser().getUserid();
        if (!userId.equals(SecurityUtil.getUser().getUserid())) {
            throw new CustomException(ErrorCode.PERMISSION_NOT_GRANTED_ERROR, "해당 답변을 삭제할 수 있는 권한이 없습니다.");
        }
        if (answer.isSelected()) {
            throw new CustomException(ErrorCode.DELETE_NOT_PERMITTED_ERROR, "채택된 답변은 삭제할 수 없습니다. ");
        }
        answerRepository.delete(answer);
    }

    @Override
    @Transactional
    public void selectAnswer(Long id) {
        Optional<Answer> optionalAnswer = answerRepository.findById(id);
        if (optionalAnswer.isEmpty()) {
            throw new CustomException(ErrorCode.ANSWER_NOT_FOUND_ERROR, "해당하는 답변이 존재하지 않습니다. ");
        }

        Answer answer = optionalAnswer.get();
        User currentUser = SecurityUtil.getUser();
        User selectUser = answer.getQuestion().getUser();

        if (!selectUser.equals(currentUser)) {
            throw new CustomException(ErrorCode.PERMISSION_NOT_GRANTED_ERROR, "질문글 작성자만 채택할 수 있습니다. ");
        }

        if (answer.isSelected()) {
            throw new CustomException(ErrorCode.ALREADY_EXISTS_ERROR, "답변이 이미 채택되어 있습니다. ");
        }

        answer.setSelected(true);
        answerRepository.save(answer);
    }
}
