package study.ywork.mybatis.domain;

public class AccountUser extends Account {
    private static final long serialVersionUID = 1L;
    private String username;
    private String address;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return super.toString() + " AccountUser{" + "username='" + username + '\'' + ", address='" + address + '\''
            + '}';
    }
}
