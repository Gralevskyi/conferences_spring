package com.hralievskyi.conferences.entity.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@Transient
	@ManyToMany(mappedBy = "roles")
	private List<User> users;

	// need for SetUpDataLoader
	public Role(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Role{" + "name: " + name + "}";
	}

	@Override
	public String getAuthority() {
		return name;
	}
}
