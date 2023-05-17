package com.dtalks.dtalks.board.post.controller;

import com.dtalks.dtalks.board.post.dto.FavoriteAndRecommendStatusDto;
import com.dtalks.dtalks.board.post.service.FavoritePostService;
import com.dtalks.dtalks.board.post.service.PostService;
import com.dtalks.dtalks.board.post.dto.PostDto;
import com.dtalks.dtalks.board.post.dto.PostRequestDto;
import com.dtalks.dtalks.board.post.service.RecommendPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final FavoritePostService favoritePostService;
    private final RecommendPostService recommendPostService;

    @Operation(summary = "특정 게시글 id로 조회, 조회수 + 1 작동")
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> searchById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.searchById(id));
    }


    @Operation(summary = "모든 게시글 조회 (페이지 사용)")
    @GetMapping("/all")
    public ResponseEntity<Page<PostDto>> searchAll(@PageableDefault(size = 10, sort = "id",  direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.searchAllPost(pageable));
    }


    @Operation(summary = "특정 유저의 게시글 조회 (페이지 사용 - 기본 post id로 정렬)", parameters = {
            @Parameter(name = "id", description = "조회할 유저의 id (db에 저장된 primary key)")
    })
    @GetMapping("/list/user/{id}")
    public ResponseEntity<Page<PostDto>> searchPostsByUser(@PathVariable Long id,
                                                           @PageableDefault(size = 10, sort = "id",  direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.searchPostsByUser(id, pageable));
    }


    @Operation(summary = "검색어로 게시글 검색", description = "keyword가 제목(title), 내용(content)에 포함되면 검색 게시글에 추가됨")
    @GetMapping("/search")
    public ResponseEntity<Page<PostDto>> searchPosts(@RequestParam String keyword,
                                                    @PageableDefault(size = 10, sort = "id",  direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.searchByWord(keyword, pageable));
    }


    @Operation(summary = "추천수 베스트 5 게시글 가져오기 (리스트)")
    @GetMapping("/best")
    public ResponseEntity<List<PostDto>> search5BestPosts() {
        return ResponseEntity.ok(postService.search5BestPosts());
    }


    @Operation(summary = "게시글 생성")
    @PostMapping
    public ResponseEntity<Long> createPost(@Valid @RequestBody PostRequestDto postDto) {
        return ResponseEntity.ok(postService.createPost(postDto));
    }


    @Operation(summary = "게시글 수정")
    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePost(@Valid @RequestBody PostRequestDto postDto, @PathVariable Long id) {
        return ResponseEntity.ok(postService.updatePost(postDto, id));
    }


    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

    @Operation(summary = "게시글에 대한 사용자의 즐겨찾기, 추천 여부 확인, 로그인한 사용자일 경우에만 api 보내면 됨"
    , description = "게시글은 로그인 없이 조회가 가능하지만 로그인한 사용자의 경우에는 즐겨찾기, 추천 여부가 필요. 두 가지가 boolean 타입으로 나온다.")
    @GetMapping("/check/status/{postId}")
    public FavoriteAndRecommendStatusDto checkFavoriteAndRecommendStatus(@PathVariable Long postId) {
        boolean favorite = favoritePostService.checkFavorite(postId);
        boolean recommend = recommendPostService.checkRecommend(postId);

        return FavoriteAndRecommendStatusDto.builder()
                .favorite(favorite)
                .recommend(recommend)
                .build();
    }


    @Operation(summary = "게시글 즐겨찾기 설정", parameters = {
            @Parameter(name = "id", description = "즐겨찾기할 게시글의 id")
    })
    @PostMapping("/favorite/{id}")
    public void favorite(@PathVariable Long id) {
        favoritePostService.favorite(id);
    }


    @Operation(summary = "게시글 즐겨찾기 취소", parameters = {
            @Parameter(name = "id", description = "즐겨찾기를 취소할 게시글의 id")
    })
    @DeleteMapping("/favorite/{id}")
    public void unFavorite(@PathVariable Long id) {
        favoritePostService.unFavorite(id);
    }


    @Operation(summary = "게시글 추천", parameters = {
            @Parameter(name = "id", description = "추천할 게시글의 id")
    })
    @PostMapping("/recommend/{id}")
    public void recommend(@PathVariable Long id) {
        recommendPostService.recommend(id);
    }


    @Operation(summary = "게시글 추천 취소", parameters = {
            @Parameter(name = "id", description = "추천을 취소할 게시글의 id")
    })
    @DeleteMapping("/recommend/{id}")
    public void cancelRecommend(@PathVariable Long id) {
        recommendPostService.cancelRecommend(id);
    }
}
