package org.bitvault.appstore.cloud.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;


/**
 * The persistent class for the user_activity_type database table.
 * 
 */
@Entity
@Table(name="user_activity_type")
@NamedQuery(name="UserActivityType.findAll", query="SELECT u FROM UserActivityType u")
public class UserActivityType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="activity_id", unique=true, nullable=false)
	private int activityId;

	@Column(name="activity_type", nullable=false, length=255)
	private String activityType;

//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="created_at", nullable=false)
//	private Date createdAt;

	@Column(nullable=false, length=1)
	private String status;
	
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	protected Date createdAt;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	protected Date updatedAt;


//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="updated_at", nullable=false)
//	private Date updatedAt;

	//bi-directional many-to-one association to UserActivity
//	@OneToMany(mappedBy="userActivityType")
//	private List<UserActivity> userActivities;
//
//	public UserActivityType() {
//	}

	public int getActivityId() {
		return this.activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public String getActivityType() {
		return this.activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

//	public Date getCreatedAt() {
//		return this.createdAt;
//	}
//
//	public void setCreatedAt(Date createdAt) {
//		this.createdAt = createdAt;
//	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	
	
	
//	public List<UserActivity> getUserActivities() {
//		return this.userActivities;
//	}
//
//	public void setUserActivities(List<UserActivity> userActivities) {
//		this.userActivities = userActivities;
//	}

//	public UserActivity addUserActivity(UserActivity userActivity) {
//		getUserActivities().add(userActivity);
//		userActivity.setUserActivityType(this);
//
//		return userActivity;
//	}
//
//	public UserActivity removeUserActivity(UserActivity userActivity) {
//		getUserActivities().remove(userActivity);
//		userActivity.setUserActivityType(null);
//
//		return userActivity;
//	}

}