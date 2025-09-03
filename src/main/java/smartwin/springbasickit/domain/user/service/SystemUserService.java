package smartwin.springbasickit.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartwin.springbasickit.common.exception.DuplicateException;
import smartwin.springbasickit.common.exception.UserRoleNotFoundException;
import smartwin.springbasickit.common.exception.code.ErrorCode;
import smartwin.springbasickit.domain.user.dto.UserSignUpDto;
import smartwin.springbasickit.domain.user.entity.SystemRole;
import smartwin.springbasickit.domain.user.entity.SystemUserEntity;
import smartwin.springbasickit.domain.user.entity.SystemUserRole;
import smartwin.springbasickit.domain.user.repository.SystemRoleRepository;
import smartwin.springbasickit.domain.user.repository.SystemUserRepository;
import smartwin.springbasickit.domain.user.repository.SystemUserRoleRepository;

@Service
@RequiredArgsConstructor
public class SystemUserService {

    private final SystemUserRepository systemUserRepository;
    private final SystemUserRoleRepository systemUserRoleRepository;
    private final SystemRoleRepository systemRoleRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(UserSignUpDto userSignUpDto) {

        systemUserRepository.findByLoginId(userSignUpDto.loginId()).ifPresent(systemUser -> {
            throw new DuplicateException(ErrorCode.DUPLICATE_ID);
        });

        SystemUserEntity userEntity = SystemUserEntity.builder()
                                                      .email(userSignUpDto.email())
                                                      .name(userSignUpDto.name())
                                                      .loginId(userSignUpDto.loginId())
                                                      .loginPassword(passwordEncoder.encode(userSignUpDto.password()))
                                                      .nickname(userSignUpDto.nickname())
                                                      .build();

        SystemUserEntity save = systemUserRepository.save(userEntity);

        SystemRole roleUser = systemRoleRepository.findByRole("ROLE_USER").stream().findFirst().orElseThrow(
                UserRoleNotFoundException::new);
        SystemUserRole systemUserRole = SystemUserRole.builder()
                                                      .systemUserId(save.getId())
                                                      .systemRoleId(roleUser.getId())
                                                      .build();

        systemUserRoleRepository.save(systemUserRole);
    }
}
