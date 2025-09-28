package ru.ivk.web.dto;

import lombok.*;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BoardDTO {
    @Min(3)
    @Max(20)
    private int size;
    @Size(min = 9, max = 400)
    @NotBlank
    private String data;
    @Pattern(regexp = "[wbWB]")
    @NotBlank
    private String nextPlayerColor;
}
