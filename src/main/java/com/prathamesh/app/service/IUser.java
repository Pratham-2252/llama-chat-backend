package com.prathamesh.app.service;

import java.util.UUID;

import com.prathamesh.app.dto.UserInfo;

public interface IUser {

	public void save(UserInfo userInfo);
	
	public void update(UUID userId, UserInfo userInfo);
}
