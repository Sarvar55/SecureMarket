package com.codems.securemarket.catalog.internal.application.port.in.command;

public record ChangeCategoryStatusCommand(Long categoryId, boolean active, Long actorId) {
}
