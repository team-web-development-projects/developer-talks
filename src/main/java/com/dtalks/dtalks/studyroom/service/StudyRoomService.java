package com.dtalks.dtalks.studyroom.service;

import com.dtalks.dtalks.studyroom.dto.StudyRoomJoinResponseDto;
import com.dtalks.dtalks.studyroom.dto.StudyRoomRequestDto;
import com.dtalks.dtalks.studyroom.dto.StudyRoomResponseDto;
import com.dtalks.dtalks.studyroom.enums.StudyRoomLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudyRoomService {
    public StudyRoomResponseDto createStudyRoom(StudyRoomRequestDto studyRoomRequestDto);

    public StudyRoomResponseDto findStudyRoomById(Long studyRoomId);

    public Page<StudyRoomResponseDto> getStudyRooms(String search, Pageable pageable);

    public StudyRoomResponseDto updateStudyRoom(Long id, StudyRoomRequestDto studyRoomRequestDto);

    public StudyRoomResponseDto joinStudyRoom(Long id);

    public void deleteStudyRoom(Long id);

    public Page<StudyRoomJoinResponseDto> studyRoomRequestList(Pageable pageable);

    public StudyRoomResponseDto acceptJoinStudyRoom(Long studyRoomId, Long studyRoomUserId, boolean status);

    public void deleteStudyRoomUser(Long id);

    public StudyRoomResponseDto expelStudyRoomUser(Long studyRoomId, String nickname);

    public Page<StudyRoomResponseDto> JoinedStudyRoomList(Pageable pageable);

    public StudyRoomResponseDto changeAuthority(Long studyRoomId, Long studyRoomUserId, StudyRoomLevel studyRoomLevel);
}
