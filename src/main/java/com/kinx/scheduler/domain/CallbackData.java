package com.kinx.scheduler.domain;

import com.kinx.scheduler.dto.GetPostResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="test_cbd")
public class CallbackData {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @Column
    private Integer categoryId;
    @Column
    private String imgPath;
    @Column
    private String description;
    @Column
    private String mediaId;
    @Column
    private String objectId;
    @Column
    private String alias;
    @Column
    private String duration;
    @Column
    private Boolean Success;
    @Column
    private String name;

    @Builder
    public CallbackData(GetPostResponse data) {
        this.categoryId = data.getCategoryId();
        this.imgPath = data.getImgPath();
        this.description = data.getDescription();
        this.mediaId = data.getMediaId();
        this.objectId = data.getObjectId().toString();
        this.alias = data.getAlias();
        this.duration = data.getDuration();
        this.Success = data.getSuccess();
        this.name = data.getName();
    }
}
