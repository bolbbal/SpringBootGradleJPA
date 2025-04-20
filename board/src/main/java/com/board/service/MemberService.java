package com.board.service;

import com.board.constant.Role;
import com.board.dto.MemberDto;
import com.board.entity.Member;
import com.board.repository.CommentRepository;
import com.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommentRepository commentRepository;

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public void saveMember(MemberDto memberDto) {

        Member member = Member.builder()
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .role(Role.USER)
                .build();

        memberRepository.save(member);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        if(member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    public Member MemberInfoByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email);
    }

    @Transactional
    public void updateMemberInfoByUsername(String email, String newName) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        member.setName(newName);

    }

    public boolean passwordChkByUsername(String email, String password) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        return passwordEncoder.matches(password, member.getPassword());

    }

    @Transactional
    public void updatePasswordByUsername(String email, String newPassword) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        member.setPassword(passwordEncoder.encode(newPassword));

    }

    @Transactional
    public boolean deleteMember(String email) {

        Member member = memberRepository.findByEmail(email);

        boolean result = false;

        if (member != null){

            commentRepository.deleteByMemberId(member.getId());

            memberRepository.deleteById(member.getId());

            result = true;
        }

        return result;

    }
}
