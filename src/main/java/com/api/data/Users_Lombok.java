package com.api.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class Users_Lombok {
    private String id;
    private String email;
    private String name;
    private String gender;
    private String status;
}
