package com.avab.avab.utils;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class RandomUsername {

    private final String[] ADJECTIVES = {
        "여가의", "휴식적인", "즐거운", "활기찬", "재미있는", "활동적인", "건강한", "외출의", "휴양의", "자연의", "문화적인", "사회적인",
        "창의적인", "배움의", "체험적인", "감각적인", "신선한", "자유로운", "편안한", "재미나는", "유익한", "의미있는", "흥미로운", "평화로운",
        "바쁜", "모험적인", "경험적인", "열정적인", "포근한", "자유분방한", "역동적인", "고요한", "융통성있는", "친밀한", "자기계발적인",
        "욕구충족의", "확장된", "몰입적인", "사색적인", "낭만적인", "이론적인", "정서적인", "동기부여의", "협동적인", "감성적인", "실용적인",
        "도전적인", "장난기 있는", "친근한", "개방적인", "능동적인", "철학적인", "심리적인", "건전한", "자기 발견의", "인상적인", "정신적인",
        "자기 실현의", "흥미진진한", "감사의", "인간적인", "세련된", "진지한", "명상적인", "평온한", "만족스러운",
    };

    private final String[] NOUNS = {
        "제노", "아델리", "잉카", "왕", "크레스트드", "뉴질랜드", "절벽", "사하린", "핀", "도미니카", "비키니", "흰털", "무릎", "양키",
        "꼬마",
    };

    public String generate() {
        Random random = new Random();
        String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[random.nextInt(NOUNS.length)];

        return adjective + " " + noun;
    }
}
