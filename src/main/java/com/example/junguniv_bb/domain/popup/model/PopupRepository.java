package com.example.junguniv_bb.domain.popup.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PopupRepository extends JpaRepository<Popup, Long> {
    Page<Popup> findByPopupNameContainingIgnoreCase(String popupName, Pageable pageable);
}

