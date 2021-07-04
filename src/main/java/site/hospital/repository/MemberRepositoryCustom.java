package site.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.dto.MemberSearchResult;
import site.hospital.dto.MemberSearchCondition;

import java.util.List;

public interface MemberRepositoryCustom {
    Page<MemberSearchResult> search(MemberSearchCondition condition, Pageable pageable);
}
