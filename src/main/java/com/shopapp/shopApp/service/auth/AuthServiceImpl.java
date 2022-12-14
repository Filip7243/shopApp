package com.shopapp.shopApp.service.auth;

import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.dto.LoginRequest;
import com.shopapp.shopApp.email.EmailSenderImpl;
import com.shopapp.shopApp.exception.token.ConfirmationTokenNotFoundException;
import com.shopapp.shopApp.exception.token.PasswordResetTokenException;
import com.shopapp.shopApp.exception.user.UserExistsException;
import com.shopapp.shopApp.exception.user.UserNotFoundException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.ConfirmationToken;
import com.shopapp.shopApp.model.PasswordResetToken;
import com.shopapp.shopApp.repository.AppUserRepository;
import com.shopapp.shopApp.repository.PasswordResetTokenRepository;
import com.shopapp.shopApp.security.CustomPasswordEncoder;
import com.shopapp.shopApp.security.jwt.JwtResponse;
import com.shopapp.shopApp.security.jwt.JwtUtils;
import com.shopapp.shopApp.service.appuser.AppUserServiceImpl;
import com.shopapp.shopApp.service.confirmationtoken.ConfirmationTokenServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.shopapp.shopApp.constants.ExceptionsConstants.*;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final EmailSenderImpl emailSender;
    private final ConfirmationTokenServiceImpl confirmationTokenService;
    private final CustomPasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final AppUserServiceImpl userService;

    @Override
    public JwtResponse signInUser(LoginRequest loginRequest) throws AuthenticationException {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.generateJwtAccessToken(authentication);
        String refreshToken = jwtUtils.generateJwtRefreshToken(authentication);
        String username = jwtUtils.getUsernameFromJwtToken(accessToken);

        AppUser user = (AppUser) authentication.getPrincipal();
        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        return new JwtResponse(
                username,
                accessToken,
                refreshToken,
                user.getUserCode(),
                authorities
        );
    }

    @Override
    public void signUpUser(AppUserSaveUpdateDto registerRequest) {
        String email = registerRequest.getEmail();

        if (userRepository.existsByEmail(email)) {
            throw new UserExistsException(String.format(USER_ALREADY_EXISTS, email));
        }

        AppUser newUser = userService.createUser(registerRequest);

        ConfirmationToken confirmationToken = new ConfirmationToken(
                null,
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                false,
                newUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String fullName = newUser.getName() + " " + newUser.getLastName();
        String link = "http://localhost:8080/api/auth/confirm?token=" + confirmationToken.getToken();
        emailSender.sendEmail(email, buildEmail(fullName, link), "Confirm your email address");

        newUser.setPassword(passwordEncoder.passwordEncoder().encode(newUser.getPassword()));
        userRepository.save(newUser);
    }

    @Override
    public void forgetPassword(String email) {
        // user gives email, on email he receives link that redirects him to reset pw site
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, email)));

        PasswordResetToken token = new PasswordResetToken(
                null,
                UUID.randomUUID().toString(),
                LocalDateTime.now().plusDays(1),
                false,
                user
        );

        passwordResetTokenRepository.save(token);

        String link = "http://localhost:8080/api/auth/reset/password?token=" + token.getToken();
        emailSender.sendEmail(email, link, "Restart password");
    }

    public void resetPassword(String token) {
        // checks if token exists
        PasswordResetToken foundToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ConfirmationTokenNotFoundException(TOKEN_NOT_FOUND));
        // checks if token used
        if (foundToken.getIsPasswordReset()) {
            throw new PasswordResetTokenException("Token already used");
        }
        // checks if token expired
        if (foundToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new PasswordResetTokenException("Token already expired!");
        }
        // setting new password from user input
        AppUser user = foundToken.getUser();
        user.setPassword(passwordEncoder.passwordEncoder().encode("password-from-input"));
        foundToken.setIsPasswordReset(true);
        passwordResetTokenRepository.save(foundToken);
        userRepository.save(user);
    }


    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
