package com.himedia.mc;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberDAO {
	void insert(String userid, String passwd, String name, String birthday,
				String gender, String region, String favorate, 
				String mobile);
	int loginCheck(String userid, String passwd);
	
}
