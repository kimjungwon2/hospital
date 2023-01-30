package site.hospital.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
import site.hospital.domain.hospital.BusinessCondition;
import site.hospital.domain.hospital.Hospital;
import site.hospital.domain.member.*;
import site.hospital.repository.AuthorityRepository;
import site.hospital.repository.MemberAuthorityRepository;
import site.hospital.repository.member.MemberRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

}