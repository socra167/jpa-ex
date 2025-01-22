package com.jpa.domain.member.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jpa.domain.member.entity.Member;
import com.jpa.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {
	private final MemberRepository memberRepository;

	public Member join(String username, String password, String nickname) {
		Member member = Member.builder()
			.username(username)
			.password(password)
			.nickname(nickname)
			.build();
		memberRepository.save(member);
		return member;
	}

	public Optional<Member> findByUsername(String username) {
		return memberRepository.findByUsername(username);
	}
}
