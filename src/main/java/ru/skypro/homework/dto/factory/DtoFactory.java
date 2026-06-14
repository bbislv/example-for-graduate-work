package ru.skypro.homework.dto.factory;

import ru.skypro.homework.constant.ApiConstants;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

import java.util.Collections;

public final class DtoFactory {

    private DtoFactory() {
    }

    public static Ad emptyAd() {
        return new Ad();
    }

    public static Ads emptyAds() {
        Ads ads = new Ads();
        ads.setCount(ApiConstants.EMPTY_COUNT);
        ads.setResults(Collections.emptyList());
        return ads;
    }

    public static ExtendedAd emptyExtendedAd() {
        return new ExtendedAd();
    }

    public static Comment emptyComment() {
        return new Comment();
    }

    public static Comments emptyComments() {
        Comments comments = new Comments();
        comments.setCount(ApiConstants.EMPTY_COUNT);
        comments.setResults(Collections.emptyList());
        return comments;
    }

    public static User emptyUser() {
        return new User();
    }

    public static UpdateUser emptyUpdateUser() {
        return new UpdateUser();
    }
}
