package com.globits.hr.timesheet.service;

import com.globits.hr.dto.search.SearchDto;
import com.globits.hr.timesheet.dto.LabelDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface LabelService {
    LabelDto getOneLabel(UUID id);

    Boolean deleteOne(UUID id);

    Page<LabelDto> searchByPage();

    List<LabelDto> saveList(List<LabelDto> dtoList);

    LabelDto saveOrUpdate(LabelDto dto, UUID id);

    Boolean checkCode(UUID id, String code);

    List<LabelDto> getAllLabel();

    List<LabelDto> getAllLabelByProjectId(UUID id);
}
