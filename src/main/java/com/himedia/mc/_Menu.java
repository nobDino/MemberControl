package com.himedia.mc;

import java.awt.Menu;
import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface _Menu {
	ArrayList<MenuItem> getList();
	void insertMenu(String x, int y);
	void deleteMenu(int x);
	void updateMenu(int x, String y, int z);
}
