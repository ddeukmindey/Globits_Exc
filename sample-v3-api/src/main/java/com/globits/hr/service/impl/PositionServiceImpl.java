package com.globits.hr.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.Position;
import com.globits.hr.dto.PositionDto;
import com.globits.hr.repository.PositionRepository;
import com.globits.hr.service.PositionService;
import com.globits.security.domain.User;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class PositionServiceImpl extends GenericServiceImpl<Position, UUID> implements PositionService {
    @Autowired
    private PositionRepository positionRepository;

    public PositionDto findByID(UUID id) {
        Position entity = null;
        Optional<Position> optional = positionRepository.findById(id);
        if (optional.isPresent()) {
            entity = optional.get();
        }
        if (entity != null) {
            return new PositionDto(entity);
        }
        return null;
    }

    public PositionDto updatePosition(PositionDto position, UUID positionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User modifiedUser = null;
        LocalDateTime currentDate = LocalDateTime.now();
        String currentUserName = "Unknown User";
        if (authentication != null) {
            modifiedUser = (User) authentication.getPrincipal();
            currentUserName = modifiedUser.getUsername();
        }
        Position updatePosition = null;
        Optional<Position> optional = positionRepository.findById(positionId);
        if (optional.isPresent()) {
            updatePosition = optional.get();
        }
        if (updatePosition != null) {
            updatePosition.setModifyDate(currentDate);
            if (position.getName() != null)
                updatePosition.setName(position.getName());
            if (position.getDescription() != null)
                updatePosition.setDescription(position.getDescription());
            if (position.getStatus() > 0)
                updatePosition.setStatus(position.getStatus());
            if (modifiedUser != null) {
                updatePosition.setModifiedBy(currentUserName);
            } else {
                updatePosition.setModifiedBy("Unknown user modified");
            }
            updatePosition = positionRepository.save(updatePosition);
            return new PositionDto(updatePosition);
        }
        return null;
    }
}
