package com.kinx.scheduler.repository;

import com.kinx.scheduler.domain.CallbackData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallbackRepository extends JpaRepository<CallbackData, Long> {
}
