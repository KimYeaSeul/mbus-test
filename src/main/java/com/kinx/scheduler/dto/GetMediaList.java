package com.kinx.scheduler.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class GetMediaList {
    private Integer category_id;
    private String next;
    private GetMediaListArray[] media_list;
}
