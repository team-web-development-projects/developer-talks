package com.dtalks.dtalks.admin.report.service;

import com.dtalks.dtalks.admin.report.dto.ReportDetailDto;
import com.dtalks.dtalks.admin.report.dto.ReportedUserDto;
import com.dtalks.dtalks.report.enums.ResultType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminReportService {
    Page<ReportedUserDto> searchAllUserReports(Pageable pageable);
    Page<ReportDetailDto> getAllReportsByUser(Long reportedUserId, Pageable pageable);
    ResultType handleReport(Long reportedUserId, ResultType resultType);
}
