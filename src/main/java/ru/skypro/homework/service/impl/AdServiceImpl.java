package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.exception.ResourceNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AccessChecker;
import ru.skypro.homework.service.AdService;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private static final String AD_NOT_FOUND = "Объявление не найдено";
    private static final String USER_NOT_FOUND = "Пользователь не найден";

    private final AdRepository adRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;
    private final AccessChecker accessChecker;

    @Override
    @Transactional(readOnly = true)
    public Ads getAllAds() {
        return adMapper.toAdsDto(adRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Ads getAdsByCurrentUser(String email) {
        ru.skypro.homework.entity.User author = getUserEntity(email);
        return adMapper.toAdsDto(adRepository.findAllByAuthorId(author.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public ExtendedAd getAdById(Integer id) {
        return adMapper.toExtendedDto(getAdEntity(id));
    }

    @Override
    @Transactional
    public Ad createAd(String email, CreateOrUpdateAd createOrUpdateAd, MultipartFile image) {
        ru.skypro.homework.entity.User author = getUserEntity(email);
        ru.skypro.homework.entity.Ad ad = adMapper.toEntity(createOrUpdateAd);
        ad.setAuthor(author);
        return adMapper.toDto(adRepository.save(ad));
    }

    @Override
    @Transactional
    public Ad updateAd(Integer id, String email, CreateOrUpdateAd createOrUpdateAd) {
        ru.skypro.homework.entity.User currentUser = getUserEntity(email);
        ru.skypro.homework.entity.Ad ad = getAdEntity(id);
        accessChecker.checkAdAccess(ad, currentUser);
        adMapper.updateEntityFromDto(createOrUpdateAd, ad);
        return adMapper.toDto(adRepository.save(ad));
    }

    @Override
    @Transactional
    public void deleteAd(Integer id, String email) {
        ru.skypro.homework.entity.User currentUser = getUserEntity(email);
        ru.skypro.homework.entity.Ad ad = getAdEntity(id);
        accessChecker.checkAdAccess(ad, currentUser);
        commentRepository.deleteAll(commentRepository.findAllByAdId(id));
        adRepository.delete(ad);
    }

    @Override
    @Transactional
    public void updateAdImage(Integer id, String email, MultipartFile image) {
        ru.skypro.homework.entity.User currentUser = getUserEntity(email);
        ru.skypro.homework.entity.Ad ad = getAdEntity(id);
        accessChecker.checkAdAccess(ad, currentUser);
    }

    private ru.skypro.homework.entity.Ad getAdEntity(Integer id) {
        return adRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AD_NOT_FOUND));
    }

    private ru.skypro.homework.entity.User getUserEntity(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
    }
}
