package com.nixmash.shiro.utils;

import com.nixmash.shiro.models.SocialUser;

public class TwitterTestUtils {

    public static SocialUser getSocialUser(String providerUserid) {
        return new SocialUser(providerUserid,
                "daveburkevt",
                "twitter",
                providerUserid,
                "Dave Burke",
                "https://twitter.com/daveburkevt",
                "https://pbs.twimg.com/profile_images/pic.jpg",
                providerUserid + "-abcdef",
                "ABCDEF"
        );

    }
}
