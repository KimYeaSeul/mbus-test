package com.kinx.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class GetPostResponse {
    @JsonProperty("CategoryId")
    private Integer categoryId;
    @JsonProperty("ImgPath")
    private String imgPath;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("MediaId")
    private String mediaId;
    @JsonProperty("ObjectId")
    private Object objectId;
    @JsonProperty("Alias")
    private String alias;
    @JsonProperty("Duration")
    private String duration;
    @JsonProperty("Success")
    private Boolean Success;
    @JsonProperty("Name")
    private String name;
}
