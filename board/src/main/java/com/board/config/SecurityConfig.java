package com.board.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    //パスワードの暗号化
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //オブジェクト間のマッピング
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //CSRFトークンを保存
        http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .formLogin(formlogin -> formlogin
                        .loginPage("/members/login")
                        .usernameParameter("email") //ログインする時、IDとして使用するパラメータ
                        .failureUrl("/members/login/error")
                        .defaultSuccessUrl("/"))
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                        .logoutSuccessUrl("/").invalidateHttpSession(true)) //ログアウトする時、セッション無効か
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/members/login").permitAll() //全員アクセス可能
                        .requestMatchers("/members/delete").authenticated() //認証使用者だけがアクセス可能
                        .requestMatchers("/members/myPage").authenticated()
                        .requestMatchers("/members/infoChange").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN") //管理者だけがアクセス可能
                        .requestMatchers("/boards/write").authenticated()
                        .requestMatchers("/boards/update").authenticated()
                        .requestMatchers("/boards/delete").authenticated()
                        .requestMatchers("/comment/save").authenticated()
                        .anyRequest().permitAll());

        return http.build();
    }

}
