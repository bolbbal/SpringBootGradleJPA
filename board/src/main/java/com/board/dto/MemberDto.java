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

    @NotBlank(message = "メールを入力してください。")
    @Email(message = "メールの形で入力してください。")
    private String email;

    @NotEmpty(message = "パスワードを入力してください。")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "パスワードは英語の大文字と小文字、特殊文字の組み合わせで８文～20文の形で入力してください。")
    private String password;

}
