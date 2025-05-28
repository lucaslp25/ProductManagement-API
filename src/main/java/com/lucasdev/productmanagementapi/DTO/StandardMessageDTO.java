package com.lucasdev.productmanagementapi.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StandardMessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    //making the format hour more friendly
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant timestamp;
    private String status;
    private String message;
    private String path;
}
//a simple class that show a default successfully message for the final user, example when delete a category i have a personalized message for show in log