package com.codems.securemarket.identity.internal.adapter.in.web;

import com.codems.securemarket.identity.internal.adapter.in.web.request.LoginRequest;
import com.codems.securemarket.identity.internal.adapter.in.web.request.RegisterRequest;
import com.codems.securemarket.identity.internal.application.port.in.query.AccessTokenView;
import com.codems.securemarket.identity.internal.application.port.in.LoginUseCase;
import com.codems.securemarket.identity.internal.application.port.in.RegisterUserUseCase;
import com.codems.securemarket.identity.internal.application.port.in.query.UserView;
import com.codems.securemarket.identity.internal.application.port.in.command.LoginCommand;
import com.codems.securemarket.identity.internal.application.port.in.command.RegisterUserCommand;
import com.codems.securemarket.shared.constants.ApplicationConstants;
import com.codems.securemarket.shared.web.response.BaseResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth", version = ApplicationConstants.DEFAULT_API_VERSION)
@SecurityRequirements
class AuthenticationController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;

    AuthenticationController(
            RegisterUserUseCase registerUserUseCase,
            LoginUseCase loginUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/register")
    ResponseEntity<BaseResponse<UserView>> register(@Valid @RequestBody RegisterRequest request) {
        var result = registerUserUseCase.register(
                new RegisterUserCommand(request.email(), request.password()));
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success(result));
    }

    @PostMapping("/login")
    BaseResponse<AccessTokenView> login(@Valid @RequestBody LoginRequest request) {
        var result = loginUseCase.login(new LoginCommand(request.email(), request.password()));
        return BaseResponse.success(result);
    }
}
