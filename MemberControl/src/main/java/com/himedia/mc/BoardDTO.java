package com.himedia.mc;

import lombok.Data;

@Data
public class BoardDTO {
	int id;
	String title;
	String content;
	String writer;
	String created;
	String updated;
}
