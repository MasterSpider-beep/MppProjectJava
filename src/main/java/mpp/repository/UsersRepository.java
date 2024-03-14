package mpp.repository;

public interface UsersRepository {
    public boolean existsUser(String username, String password);
}
