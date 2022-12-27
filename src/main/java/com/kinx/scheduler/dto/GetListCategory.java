package com.kinx.scheduler.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetListCategory {
    private int category_id;
    private String category_name;
    private int bytes;
    private boolean basic;
    private boolean save_origin;
    private String channel_id;
    private String created;
    private String modified;
}
