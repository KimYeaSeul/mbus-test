package com.kinx.scheduler.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class ResponseDto {

    private Map<String, List<GetListCategory>> attributes;
    private List<GetListCategory> attributeslistCategory;

    public ResponseDto(Map<String, List<GetListCategory>> attributes){
        this.attributes = attributes;
        this.attributeslistCategory = attributes.get("category_list");
    }

}
