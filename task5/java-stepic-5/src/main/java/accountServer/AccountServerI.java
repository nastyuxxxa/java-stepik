package accountServer;

public interface AccountServerI {
    void addNewUser();

    void removeUser();

    int getUsersLimit();

    void setUsersLimit(int userLimit);

    int getUsersCount();
}