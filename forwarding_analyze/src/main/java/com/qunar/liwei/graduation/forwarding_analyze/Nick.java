package com.qunar.liwei.graduation.forwarding_analyze;

class Nick {
	static int id = 1;
	int parentId;
	int nickID;
	int deep;
	String nickName;
	String reason;
	public Nick(int parentId, int deep,String nickName, String reason) {
		super();
		this.parentId = parentId;
		this.nickID = id++;
		this.deep = deep;
		this.nickName = nickName;
		this.reason = reason;
	}
	
	
}
