package com.dtalks.dtalks.post.entity;

import com.dtalks.dtalks.base.entity.BaseTimeEntity;
import com.dtalks.dtalks.post.dto.PostRequestDto;
import com.dtalks.dtalks.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Builder
    public static Post toEntity(PostRequestDto postDto, User user) {
        return Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .user(user)
                .build();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
