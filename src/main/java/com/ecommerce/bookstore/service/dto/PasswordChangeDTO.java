package com.ecommerce.bookstore.service.dto;

/**
 * A DTO representing a password change that requires current and new password
 * data.
 */
public class PasswordChangeDTO {
    private String currentPassword;
    private String newPassword;

    public PasswordChangeDTO() {
    }

    public PasswordChangeDTO(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
