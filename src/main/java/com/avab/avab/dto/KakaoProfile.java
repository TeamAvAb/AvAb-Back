package com.avab.avab.dto;

import lombok.Getter;

@Getter
public class KakaoProfile {

    private Long id;
    private String connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Getter
    public class Properties {

        private String nickname;
    }

    @Getter
    public class KakaoAccount {

        private Boolean profile_nickname_needs_agreement;
        private Profile profile;
        private Boolean name_needs_agreement;
        private String name;
        private Boolean has_email;
        private Boolean email_needs_agreement;
        private Boolean is_email_valid;
        private Boolean is_email_verified;
        private String email;

        public class Profile {

            private String nickname;
        }
    }
}
