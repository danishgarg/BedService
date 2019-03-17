package com.dg.bedservice.infrastructure.web.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBedRequest {
    private String id;
    private String location;
}