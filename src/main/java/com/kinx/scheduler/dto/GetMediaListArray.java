package com.kinx.scheduler.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetMediaListArray {
    private String media_id;
    private String media_name;
    private String duration;
    private String poster_url;
    private String created;
}
