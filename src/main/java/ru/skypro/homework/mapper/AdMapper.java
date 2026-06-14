package ru.skypro.homework.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.skypro.homework.constant.ApiConstants;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdMapper {

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    Ad toDto(ru.skypro.homework.entity.Ad ad);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "author.email", target = "email")
    @Mapping(source = "author.phone", target = "phone")
    ExtendedAd toExtendedDto(ru.skypro.homework.entity.Ad ad);

    List<Ad> toDtoList(List<ru.skypro.homework.entity.Ad> ads);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "comments", ignore = true)
    ru.skypro.homework.entity.Ad toEntity(CreateOrUpdateAd createOrUpdateAd);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "comments", ignore = true)
    void updateEntityFromDto(CreateOrUpdateAd createOrUpdateAd, @MappingTarget ru.skypro.homework.entity.Ad ad);

    default Ads toAdsDto(List<ru.skypro.homework.entity.Ad> ads) {
        List<ru.skypro.homework.entity.Ad> source = Optional.ofNullable(ads).orElse(Collections.emptyList());
        Ads result = new Ads();
        result.setCount(source.size());
        result.setResults(toDtoList(source));
        return result;
    }

    default Ads emptyAdsDto() {
        Ads ads = new Ads();
        ads.setCount(ApiConstants.EMPTY_COUNT);
        ads.setResults(Collections.emptyList());
        return ads;
    }
}
