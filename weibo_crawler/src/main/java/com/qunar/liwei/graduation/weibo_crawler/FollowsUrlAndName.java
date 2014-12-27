package com.qunar.liwei.graduation.weibo_crawler;

import java.util.Set;

public class FollowsUrlAndName {
	private String name;
	private Set<String> followName;
	private Set<String> followUrl;
	public FollowsUrlAndName(String name, Set<String> followName,
			Set<String> followUrl) {
		super();
		this.name = name;
		this.followName = followName;
		this.followUrl = followUrl;
	}
	public String getName() {
		return name;
	}
	public Set<String> getFollowName() {
		return followName;
	}
	public Set<String> getFollowUrl() {
		return followUrl;
	}
	@Override
	public String toString() {
		return "NameAndFollows [name=" + name + ", followName=" + followName
				+ ", followUrl=" + followUrl + "]";
	}
	
	
}
