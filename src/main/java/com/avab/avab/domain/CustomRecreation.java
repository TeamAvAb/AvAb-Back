package com.avab.avab.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import com.avab.avab.domain.mapping.FlowRecreation;
import com.avab.avab.domain.mapping.RecreationRecreationKeyword;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomRecreation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    private String title;

    private Integer playTime;

    @OneToOne(mappedBy = "customRecreation", cascade = CascadeType.ALL)
    private FlowRecreation flowRecreation;

    @Builder.Default
    @OneToMany(mappedBy = "customRecreation", cascade = CascadeType.ALL)
    private List<RecreationRecreationKeyword> recreationRecreationKeywordList = new ArrayList<>();
}
