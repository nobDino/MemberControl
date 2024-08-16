<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>AJAX(Asynchronous Javascript And XML) CRUD</title>
</head>
<style>
table { margin:auto; border-collapse:collapsed;}
td:nth-child(1) {
	text-align:right;
}
td {
	border:1px solid black;
}
th { color:white; background-color:black;}
</style>
<body>
<input type=hidden id=id>
<table>
<tr><td>제목</td><td><input type=text id=title></td></tr>
<tr><td>내용</td><td><textarea rows=20 cols=50 id=content></textarea></td></tr>
<tr><td colspan=2 style='text-align:center;'>
	<input type=button value='등록' id=btnAdd>
	<input type=button value='비우기' id=btnClear>
	<input type=button value='삭제' id=btnDelete>
</td></tr>
</table>
<button id='btnLoad'>데이터 가져오기</button><br><br>
<table id=tblBoard>
<thead>
<tr><th>번호</th><th>제목</th><th>작성자</th><th>작성시각</th></tr>
</thead>
<tbody></tbody>
</table>
</body>
<script src="https://code.jquery.com/jquery-latest.js"></script>
<script>
$(document)
.ready(function(){
	$('#btnLoad').trigger('click');
	return false;
})
.on('click','#btnLoad',function(){
	$.ajax({
		url:'/list',type:'post',data:{},dataType:'json',
		success:function(data){
			console.log(data);
			$('#tblBoard tbody').empty();
			for( let x of data){
				let str = '<tr>';
				str+='<td>'+x['id']+'</td><td>'+x['title']+'</td><td>'+
				x['author']+'</td><td>'+x['created']+'</td></tr>';
				$('#tblBoard tbody').append(str);
			}
		},
		error:function(){},
		complete:function(){}
	})
	return false;	
})
.on('click','#btnAdd',function(){
	let title = $('#title').val();
	let content = $('#content').val();
	console.log(title,content);
	if(title=='' ||  content=='') return false;
	
	if($('#id').val()==''){
	$.ajax({
		url:'/saveBoard',type:'post',data:{title:title, content:content},dataType:'text',
		success:function(data){
			if(data=='ok'){
				$('#btnLoad,#btnClear').trigger('click');
			}
		}
	});
	} else{
		$.ajax({
			url:'/updateBoard',type:'post',data:{id:$('#id').val(),title:title,content:content},
			dataType:'text',
			success:function(data){
				if(data=='ok'){
					$('#btnLoad,#btnClear').trigger('click');
				}
			}
		})
		
	};
	return false;
})
.on('click','#btnClear',function(){
	$('#id,#title,#content').val('');
	return false;
})
.on('click','#tblBoard tbody tr',function(){
	let id = $(this).find('td:eq(0)').text();
	let title = $(this).find('td:eq(1)').text();
	$('#id').val(id);
	$('#title').val(title);
	$.ajax({
		url:'/viewBoard',type:'post',data:{id:id},dataType:'text',
		success:function(data){
			$('#content').val(data);
		}
	});
	return false;
})
.on('click','#btnDelete',function(){
	if($('#id').val()=='') {
		alert('삭제할 게시물을 먼저 선택하시오');
		return false;
	}
	if(!confirm('정말로 삭제할까요?')) return false;
	
	$.ajax({
		url:'/deleteBoard',type:'post',data:{id:$('#id').val()},dataType:'text',
		success:function(data){
			if(data=='ok'){
				$('#btnLoad,#btnClear').trigger('click');
			}
		}
	});
	return false;
})
</script>
</html>