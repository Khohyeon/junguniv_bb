package com.example.junguniv_bb.domain.managerAuth.dto;

import com.example.junguniv_bb.domain.managerAuth.model.ManagerAuth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ManagerAuthPageResDTO(List<ManagerAuthDTO> managerAuthList, PageableDTO pageable) {

    public ManagerAuthPageResDTO(Page<ManagerAuth> managerAuthPage) {
        this(
            managerAuthPage.getContent().stream()
                .map(ManagerAuthDTO::from)
                .collect(Collectors.toList()),
            new PageableDTO(managerAuthPage)
        );
    }

    public static ManagerAuthPageResDTO from(Page<ManagerAuth> managerAuthPage) {
        return new ManagerAuthPageResDTO(
            managerAuthPage.getContent().stream()
                .map(ManagerAuthDTO::from)
                .collect(Collectors.toList()),
            new PageableDTO(managerAuthPage)
        );
    }

    public record ManagerAuthDTO(
            Long authIdx,
            Long menuIdx,
            Long authLevel,
            Long menuReadAuth,
            Long menuWriteAuth,
            Long menuModifyAuth,
            Long menuDeleteAuth
    ) {
        public static ManagerAuthDTO from(ManagerAuth managerAuth) {
            return new ManagerAuthDTO(
                managerAuth.getAuthIdx(),
                managerAuth.getMenuIdx(),
                managerAuth.getAuthLevel(),
                managerAuth.getMenuReadAuth(),
                managerAuth.getMenuWriteAuth(),
                managerAuth.getMenuModifyAuth(),
                managerAuth.getMenuDeleteAuth()
            );
        }
    }

    public record PageableDTO(int pageNumber, int pageSize, int totalPages, long totalElements, boolean isLast,
            int numberOfElements, boolean isEmpty, Sort sort) {
        public PageableDTO(Page<ManagerAuth> managerAuthPage) {
            this(managerAuthPage.getNumber(), managerAuthPage.getSize(), managerAuthPage.getTotalPages(),
                    managerAuthPage.getTotalElements(), managerAuthPage.isLast(), managerAuthPage.getNumberOfElements(),
                    managerAuthPage.isEmpty(), managerAuthPage.getSort());
        }
    }
}
