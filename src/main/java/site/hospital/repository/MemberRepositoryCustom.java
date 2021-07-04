package site.hospital.repository;

import site.hospital.dto.MemberSearchResult;
import site.hospital.dto.MemberSearchCondition;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberSearchResult> search(MemberSearchCondition condition);
}
