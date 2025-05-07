package com.board.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MemberDto {

    @NotBlank(message = "名前を入力してください。") //null値、文字列の長さ０、空文字列を検査、失敗時メッセージ送信
    private String name;

    @NotBlank(message = "メールを入力してください。")
    @Email(message = "メールの形で入力してください。") //メール形ではない場合メッセージ送信
    private String email;

    @NotEmpty(message = "パスワードを入力してください。") //null値や文字列の長さ０の場合メッセージ送信
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", //空白がないし、８字以上～２０字以下で、数字、ローマ字、特殊文字を含む
            message = "パスワードは英語の大文字と小文字、特殊文字の組み合わせで８文～20文の形で入力してください。")
    private String password;

}
