package scrumtogether.scrumtogetherapi.entities.enums;

/**
 * Represents the role of a user in the system, defining their access levels and permissions.
 * <p>
 * Each role represents a distinct set of permissions:
 * <ul>
 *     <li>{@link #ADMIN} - Full system access with administrative privileges</li>
 *     <li>{@link #DEFAULT} - Standard user access with basic privileges</li>
 * </ul>
 *
 * This enum is used in conjunction with Spring Security for role-based access control (RBAC).
 * The roles are prefixed with "ROLE_" when converted to Spring Security authorities.
 */
public enum Role {
    /**
     * Administrative role with full system access.
     * Users with this role can:
     * <ul>
     *     <li>Manage other users</li>
     *     <li>Configure system settings</li>
     *     <li>Access all features</li>
     * </ul>
     */
    ADMIN,

    /**
     * Default role assigned to new users.
     * Users with this role can:
     * <ul>
     *     <li>Access basic features</li>
     *     <li>Manage their own profile</li>
     *     <li>Participate in standard activities</li>
     * </ul>
     */
    DEFAULT;

    /**
     * Returns the Spring Security authority name for this role.
     * Prefixes the role name with "ROLE_" as per Spring Security convention.
     *
     * @return the authority name string
     */
    public String getAuthority() {
        return "ROLE_" + this.name();
    }

    /**
     * Checks if this role has administrative privileges.
     *
     * @return true if this is an admin role, false otherwise
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }

    /**
     * Creates a user-friendly display name for the role.
     *
     * @return a formatted string representation of the role
     */
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
