package vn.hn.hncommonservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity {
	
	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	@Column(name = "updated_at", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime updatedAt;
	
	@CreatedBy
	@Column(name = "created_by", length = 250, updatable = false)
	private String createdBy;
	
	@LastModifiedBy
	@Column(name = "updated_by", length = 250)
	private String updatedBy;
	
	@Column(name = "deleted", nullable = false)
	private boolean deleted = false;
	
	public void softDelete() {
		this.deleted = true;
	}
	
	public void restore() {
		this.deleted = false;
	}
	
}
