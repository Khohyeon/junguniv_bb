package com.example.junguniv_bb.domain.board.model;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BbsSpecifications {

    public static Specification<Bbs> bbsGroupEquals(BbsGroup bbsGroup) {
        return (root, query, criteriaBuilder) -> {
            if (bbsGroup == null) {
                return criteriaBuilder.conjunction(); // 조건 없음
            }
            return criteriaBuilder.equal(root.get("bbsGroup"), bbsGroup);
        };
    }


    public static Specification<Bbs> titleContains(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) {
                return criteriaBuilder.conjunction(); // 조건 없음
            }
            return criteriaBuilder.like(root.get("title"), "%" + title + "%");
        };
    }
    public static Specification<Bbs> contentContains(String content) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("contents"), "%" + content + "%");
    }

    public static Specification<Bbs> titleOrContentContains(String keyword) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(root.get("title"), "%" + keyword + "%"),
                        criteriaBuilder.like(root.get("contents"), "%" + keyword + "%")
                );
    }

    public static Specification<Bbs> createdDateAfter(LocalDate startDate) {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime startDateTime = startDate.atStartOfDay(); // 하루 시작 시간
            return criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), startDateTime);
        };
    }

    public static Specification<Bbs> createdDateBefore(LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59); // 하루 끝 시간
            return criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), endDateTime);
        };
    }

    public static Specification<Bbs> categoryEquals(String category) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category"), category);
    }
}
