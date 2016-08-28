package com.rhcloud.analytics4github.domain;

/**
 * represent a commit author
 *
 * @author lyashenkogs.
 */
public class Author {
    private final String name;
    private final String email;

    /**
     * @param name  represent author name. Add an empty String of zero length, if not present
     * @param email represent author email. Add an empty String of zero length, if not present
     */
    public Author(String name, String email) {
        this.email = email;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        if (name != null ? !name.equals(author.name) : author.name != null) return false;
        return email != null ? email.equals(author.email) : author.email == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Author{");
        sb.append("name='").append(name).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
