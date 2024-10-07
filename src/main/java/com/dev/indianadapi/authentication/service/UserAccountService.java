package com.dev.indianadapi.authentication.service;

import com.dev.indianadapi.authentication.dto.UserAccountProfileResponse;
import com.dev.indianadapi.authentication.entity.UserAccount;
import com.dev.indianadapi.authentication.repository.UserAccountRepository;
import com.dev.indianadapi.film_ad.FilmAdService;
import com.dev.indianadapi.film_ad.dto.FilmAdResponse;
import com.dev.indianadapi.investment.InvestmentResponse;
import com.dev.indianadapi.investment.InvestmentService;
import com.dev.indianadapi.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountRepository repository;
    private final CustomUserDetailsService userDetailsService;
    private final FilmAdService filmAdService;
    private final S3Service s3Service;
    private final InvestmentService investmentService;

    public UserAccountProfileResponse getUserProfile(String uid, String viewerJwt) {

        UserAccount userAccount = repository.findByUid(uid).orElseThrow(
                () -> new UsernameNotFoundException("Не удалось найти пользователя с таким uid")
        );

        boolean isProfileOwner = isProfileOwner(userAccount, viewerJwt);
        List<FilmAdResponse> filmAds = filmAdService.findFilmAdsByUserAccountId(userAccount.getId());
        List<InvestmentResponse> investments = investmentService.getUserAccountInvestments(viewerJwt);

        return UserAccountProfileResponse.builder()
                .uid(uid)
                .email(isProfileOwner ? userAccount.getEmail() : null)
                .filmAds(filmAds)
                .avatarUrl(userAccount.getAvatarUrl())
                .balance(userAccount.getBalance().getBalance())
                .investments(isProfileOwner ? investments : null)
                .build();

    }

    @Transactional
    public void updateUserAccountProfile(String jwt, MultipartFile image, String newUid) {

        UserAccount userAccount = userDetailsService.findUserByJwt(jwt);

        String oldAvatarUrl = userAccount.getAvatarUrl();

        if (image != null && !image.isEmpty()) {
            String newAvatarUrl = s3Service.uploadImage(image, "avatar");
            userAccount.setAvatarUrl(newAvatarUrl);
        }

        if (newUid != null && !newUid.isEmpty()) {
            userAccount.setUid("@" + newUid);
        }

        if (oldAvatarUrl != null && !oldAvatarUrl.isEmpty()) {
            s3Service.deleteFileByUrl(oldAvatarUrl);
        }

        userDetailsService.saveUser(userAccount);

    }

    private boolean isProfileOwner(UserAccount userAccount, String viewerJwt) {
        UserAccount viewer = userDetailsService.findUserByJwt(viewerJwt);
        return viewer.equals(userAccount);
    }

}
