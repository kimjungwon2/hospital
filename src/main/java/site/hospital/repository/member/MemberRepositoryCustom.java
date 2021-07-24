package site.hospital.repository.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.repository.member.simplequery.MemberSearchResult;
import site.hospital.repository.member.simplequery.MemberSearchCondition;

public interface MemberRepositoryCustom {
    Page<MemberSearchResult> memberSearch(MemberSearchCondition condition, Pageable pageable);
}
