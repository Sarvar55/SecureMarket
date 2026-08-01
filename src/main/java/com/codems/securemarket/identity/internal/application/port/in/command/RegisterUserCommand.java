package com.codems.securemarket.identity.internal.application.port.in.command;

public record RegisterUserCommand(String email, String password) {
}
