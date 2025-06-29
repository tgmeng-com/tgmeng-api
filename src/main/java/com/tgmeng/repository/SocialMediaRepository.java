package com.tgmeng.repository;

import com.tgmeng.model.dto.topsearch.TopSearchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SocialMediaRepository {
    public List<TopSearchDTO> getSocialMedia(String topSearchKeyword) {
        return null;
    }
}
