package com.ringmabell.whichme_backend.entitiy.user;

import com.ringmabell.whichme_backend.entitiy.Member;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends Member {

    @Column(nullable = false)
    private String realName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String birthday;

    @ElementCollection(targetClass = Disease.class)
    @CollectionTable(name = "user_diseases",joinColumns = @JoinColumn(name ="user_id"))
    @Enumerated(value = EnumType.STRING)
    @Column(name = "disease")
    private Set<Disease> diseases;

}
