package learning.platform.entity;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class Profile {

    private String profilePhoto;
    private String about;

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
