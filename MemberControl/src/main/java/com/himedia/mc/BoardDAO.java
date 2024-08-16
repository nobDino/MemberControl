package com.himedia.mc;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardDAO {
    void insert(String title, String content, String writer);
    ArrayList<BoardDTO> getList(int start);
    int getCount(); // 전체 게시물 갯수 
    BoardDTO getView(int id);
    void delete(int id);
    void update(int id, String title, String content);
    void addHit(int id);
}