package vn.iotstar.entity;



import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank(message = "Full name is required")
    @Column(name = "fullname", columnDefinition = "nvarchar(255)")
    private String fullname;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Column(name = "passwd")
    private String passwd;

    @NotNull(message = "Sign-up date is required")
    @Temporal(TemporalType.DATE)
    @Column(name = "sign_up_date")
    private Date signUpDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "last_login")
    private Date lastLogin;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Video> videos;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Category> categories;
}
