package com.kinx.scheduler.repository;

import com.kinx.scheduler.domain.ReqUploadMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReqUploadMediaRepository extends JpaRepository<ReqUploadMedia, Integer> {
}
