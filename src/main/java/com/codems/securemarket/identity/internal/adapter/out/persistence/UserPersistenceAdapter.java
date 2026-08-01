package com.codems.securemarket.identity.internal.adapter.out.persistence;

import com.codems.securemarket.identity.internal.application.port.out.LoadUserPort;
import com.codems.securemarket.identity.internal.application.port.out.SaveUserPort;
import com.codems.securemarket.identity.internal.domain.exception.EmailAlreadyExistsException;
import com.codems.securemarket.identity.internal.domain.exception.UserNotFoundException;
import com.codems.securemarket.identity.internal.domain.model.Email;
import com.codems.securemarket.identity.internal.domain.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
class UserPersistenceAdapter implements LoadUserPort, SaveUserPort {

    private final JpaUserRepository repository;

    UserPersistenceAdapter(JpaUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findById(Long userId) {
        return repository.findById(userId).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return repository.findByEmail(email.value()).map(UserEntity::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return repository.existsByEmail(email.value());
    }

    @Override
    public List<User> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(UserEntity::toDomain)
                .toList();
    }

    @Override
    public User save(User user) {
        try {
            UserEntity entity;

            if (user.getId() == null) {
                entity = UserEntity.create(user);
            } else {
                entity = repository.findById(user.getId())
                        .orElseThrow(() -> new UserNotFoundException(user.getId()));
                entity.updateFrom(user);
            }

            return repository.save(entity).toDomain();
        } catch (DataIntegrityViolationException exception) {
            throw new EmailAlreadyExistsException();
        }
    }
}
