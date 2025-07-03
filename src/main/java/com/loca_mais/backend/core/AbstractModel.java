package com.loca_mais.backend.core;

import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.Date;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbstractModel {
    public int id;
    private Date createdAt=new Date();
    private Date updatedAt=new Date();
    private boolean active=true;
}