package com.avab.avab.security.kakao.dto;

import lombok.Data;

@Data
public class KakaoProfile {
    public Long id;
    public String connected_at;
    public Properties properties;
    public KakaoAccount kakao_account;

    public class Properties {

        public String nickname;
    }

    public class KakaoAccount {

        public Boolean profile_nickname_needs_agreement;
        public Profile profile;
        public Boolean name_needs_agreement;
        public String name;
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;

        public class Profile {

            public String nickname;
        }
    }
}
