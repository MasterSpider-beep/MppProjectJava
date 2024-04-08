package repository;



import domain.User;

import java.util.Optional;

public interface UsersRepository {
    public boolean checkUser(String username, String password);
    public Optional<User> getOne(String username)   ;
}
