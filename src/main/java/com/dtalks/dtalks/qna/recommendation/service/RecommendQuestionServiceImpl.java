package com.dtalks.dtalks.qna.recommendation.service;

import com.dtalks.dtalks.exception.ErrorCode;
import com.dtalks.dtalks.exception.exception.CustomException;
import com.dtalks.dtalks.qna.question.entity.Question;
import com.dtalks.dtalks.qna.question.repository.QuestionRepository;
import com.dtalks.dtalks.qna.recommendation.dto.RecommendQuestionDto;
import com.dtalks.dtalks.qna.recommendation.entitiy.RecommendQuestion;
import com.dtalks.dtalks.qna.recommendation.repository.RecommendQuestionRepository;
import com.dtalks.dtalks.user.Util.SecurityUtil;
import com.dtalks.dtalks.user.entity.User;
import com.dtalks.dtalks.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendQuestionServiceImpl implements RecommendQuestionService {
    private final RecommendQuestionRepository recommendQuestionRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    @Override
    public Long recommendQuestion(RecommendQuestionDto recommend) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.getByUserid(SecurityUtil.getCurrentUserId()));
        if (optionalUser.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND_ERROR, "존재하지 않는 사용자입니다.");
        }

        Optional<Question> optionalQuestion = questionRepository.findById(recommend.getQuestionId());
        if(optionalQuestion.isEmpty()){
            throw new CustomException(ErrorCode.POST_NOT_FOUND_ERROR, "해당하는 질문글이 존재하지 않습니다. ");
        }

        Question question = optionalQuestion.get();
        User user = optionalUser.get();

        if(recommendQuestionRepository.existsByUserAndQuestion(user,question)){
            throw new CustomException(ErrorCode.RECOMMENDATION_ALREADY_EXIST_ERROR, "이미 해당 질문글을 추천하였습니다. ");
        }

        RecommendQuestion recommendQuestion = RecommendQuestion.toEntity(user, question);

        question.updateLike(true);
        recommendQuestionRepository.save((recommendQuestion));

        return recommendQuestion.getId();
    }

    @Override
    @Transactional
    public Long unRecommendQuestion(RecommendQuestionDto unRecommend) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.getByUserid(SecurityUtil.getCurrentUserId()));
        if (optionalUser.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND_ERROR, "존재하지 않는 사용자입니다. ");
        }

        Optional<Question> optionalQuestion = questionRepository.findById(unRecommend.getQuestionId());
        if(optionalQuestion.isEmpty()){
            throw new CustomException(ErrorCode.POST_NOT_FOUND_ERROR, "해당하는 질문글이 존재하지 않습니다. ");
        }

        Question question = optionalQuestion.get();
        User user = optionalUser.get();

        if(!recommendQuestionRepository.existsByUserAndQuestion(user,question)){
            throw new CustomException(ErrorCode.RECOMMENDATION_NOT_FOUND_ERROR, "이 질문글을 추천한 적이 없습니다 . ");
        }
        RecommendQuestion recommendQuestion = RecommendQuestion.toEntity(user, question);

        question.updateLike(false);
        recommendQuestionRepository.deleteByUserAndQuestion(user, question);
        return recommendQuestion.getId();
    }

}
