package com.codems.securemarket.catalog.internal.application.port.in.command;

public record CreateCategoryCommand(String name, String slug, Long actorId) {
}
