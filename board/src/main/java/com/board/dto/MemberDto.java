package com.board.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MemberDto {

    @NotBlank(message = "名前を入力してください。")
    private String name;

    @Email(message = "メールの形で入力してください。")
    private String email;

    @NotEmpty(message = "パスワードを入力してください。")
    @Pattern(regexp = "(?=.*[a-zA-Z0-9])(?=.*\\W)(?=\\S+$)", message = "パスワードは英語の大文字と小文字、特殊文字の組み合わせで入力してください。")
    private String password;

}
