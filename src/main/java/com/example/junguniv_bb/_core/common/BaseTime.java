package com.example.junguniv_bb._core.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/* JPA Entity 클래스들이 BaseTime 상속할 경우 필드들(createdDate, modifiedDate)도 칼럼으로 인식 */
/* BaseTime 클래스에 Auditing 기능을 포함 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
public abstract class BaseTime {
    @CreatedDate
    @Column(name = "created_date", updatable = false)  // 업데이트 시 변경되지 않도록 설정
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    // 생성일을 지정된 형식으로 반환하는 메서드
    public String getFormattedCreatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return createdDate != null ? createdDate.format(formatter) : null;
    }
    public String getFormattedCreatedDate2() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return createdDate != null ? createdDate.format(formatter) : null;
    }

    // 수정일을 지정된 형식으로 반환하는 메서드
    public String getFormattedUpdatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return updatedDate != null ? updatedDate.format(formatter) : null;
    }

    // 수정일을 지정된 형식으로 반환하는 메서드
    public String getFormattedUpdatedDate2() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return updatedDate != null ? updatedDate.format(formatter) : null;
    }
}