package com.kinx.scheduler.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Entity(name="test_rum")
public class ReqUploadMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pid;
    private String filename;
    private Long size;
    private String mediaId;

    @CreationTimestamp
    private LocalDateTime createdDate;

    private LocalDateTime successDate;

    @Builder
    public ReqUploadMedia(String filename, Long size, String mediaId) {
        this.filename = filename;
        this.size = size;
        this.mediaId = mediaId;
    }

}
