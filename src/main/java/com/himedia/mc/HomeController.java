package com.himedia.mc;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired MemberDAO mdao;
	@Autowired BoardDAO bdao;
	@Autowired _Menu _menu;
	
	@GetMapping("/menuctrl")
	public String menuCtrl() {
		return "menu/crud";
	}
	@PostMapping("/getMenu") // 메뉴 리스트 출력
	@ResponseBody
	public String getMenu() {
		ArrayList<MenuItem> arMenu = _menu.getList();
		System.out.println("arMenu size="+arMenu.size());
		JSONArray ja = new JSONArray();
		for(MenuItem bdto : arMenu) {
			JSONObject jo = new JSONObject();
			jo.put("id", bdto.getId());
			jo.put("name", bdto.getName());
			jo.put("price", bdto.getPrice());
			
			ja.put(jo);
		}
		return ja.toString();
	}
	@PostMapping("/addMenu") //메뉴 추가
	@ResponseBody
	public String addMenu(HttpServletRequest req) {
		String name=req.getParameter("name");
		String price = req.getParameter("price");
		System.out.println("name="+name+", price="+price);
		_menu.insertMenu(name, Integer.parseInt(price));
		return "ok";
	}
	@PostMapping("/updateMenu") //메뉴 업데이트
	@ResponseBody
	public String updateMenu(HttpServletRequest req) {
		int id= Integer.parseInt(req.getParameter("id"));
		String name =req.getParameter("name");
		String price = req.getParameter("price");
		System.out.println("id="+id+", name="+name+", price=" + price);
		_menu.updateMenu(id, name, Integer.parseInt(price));
		return "ok";
	}
	@PostMapping("/deleteMenu") // 메뉴 삭제
	  @ResponseBody
	   public String deleteMenu(HttpServletRequest req) {
	      int id = Integer.parseInt(req.getParameter("id"));
	      _menu.deleteMenu(id);
	      return "ok";
	   }

	
	@PostMapping("/list") //게시물 리스트 출력
	@ResponseBody
	public String doList(HttpServletRequest req) {
		int start=0;
		ArrayList<BoardDTO> arBoard = bdao.getList(start);
		System.out.println("arBoard size="+arBoard.size());
		JSONArray ja = new JSONArray();
		for(BoardDTO bdto : arBoard) {
			JSONObject jo = new JSONObject();
			jo.put("id", bdto.getId());
			jo.put("title", bdto.getTitle());
			jo.put("content", bdto.getContent());
			jo.put("author", bdto.getWriter());
			jo.put("created", bdto.getCreated());
			jo.put("updated", bdto.getUpdated());
			
			ja.put(jo);
		}
		return ja.toString();
	}
	@GetMapping("/crud")
	public String crud(HttpServletRequest req) {
		HttpSession s = req.getSession();
		String userid = (String) s.getAttribute("userid");
		if(userid==null || userid.equals("")) return "redirect:/login";
		
		return "ajax/crud";
	}
	@GetMapping("/")
	public String home(HttpServletRequest req, Model model) {
		HttpSession s = req.getSession();
		String userid = (String) s.getAttribute("userid");
		String linkstr="";
		String newpost="";
		if(userid==null || userid.equals("")){
			linkstr = "<a href='/login'>로그인</a>&nbsp;&nbsp;&nbsp;<a href='/signup'>회원가입</a>";
			newpost = "";
		} else {
			linkstr = userid+"&nbsp;&nbsp;&nbsp;<a href='/logout'>로그아웃</a>";
			newpost = "<a href='/write'>새글작성</a>";
		}
		model.addAttribute("linkstr", linkstr);
		model.addAttribute("newpost", newpost);

		String pageno = req.getParameter("p");
		int nowpage=1;
		if(pageno == null || pageno.equals("")) nowpage = 1;
		else nowpage = Integer.parseInt(pageno);

		int total = bdao.getCount();
		int pagesize = 20;

		int start = (nowpage - 1) * pagesize;
		System.out.println("start=" + start);
		int lastpage = (int) Math.ceil((double) total / pagesize);
		System.out.println("lastpage=[" + lastpage + "]");

		String movestr = "<a href='/?p=1'>처음</a>&nbsp;&nbsp;";
		if (nowpage != 1) {
		    movestr += "<a href='/?p=" + (nowpage - 1) + "'>이전</a>&nbsp;&nbsp;";
		}
		if (nowpage != lastpage) {
		    movestr += "<a href='/?p=" + (nowpage + 1) + "'>다음</a>&nbsp;&nbsp;";
		}
		movestr += "<a href='/?p=" + lastpage + "'>마지막</a>";

		model.addAttribute("movestr", movestr);
		ArrayList<BoardDTO> arBoard = bdao.getList(start);
		System.out.println("size=" + arBoard.size());
		model.addAttribute("arBoard", arBoard);
		return "home";

	}
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	@PostMapping("/doLogin")
	public String doLogin(HttpServletRequest req) {
		String userid = req.getParameter("userid");
		String passwd = req.getParameter("passwd");
		if(userid==null || userid.equals("")) return "redirect:/login";
		if(passwd==null || passwd.equals("")) return "redirect:/login";
		int n = mdao.loginCheck(userid, passwd);
		System.out.println("n="+n);
		if(n>0) { // login성공
			HttpSession s = req.getSession();
			s.setAttribute("userid", userid);
			return "redirect:/";
		} else {	// login실패
			return "redirect:/login";
		}
	}
	@GetMapping("/logout")
	public String logout(HttpServletRequest req) {
		HttpSession s = req.getSession();
		s.invalidate();
		return "redirect:/";
	}
	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}
	@PostMapping("/doSignup")
	public String doSignup(HttpServletRequest req,Model model) {
		String passwd = req.getParameter("passwd");
		String passwd1 = req.getParameter("passwd1");
		if(!passwd.equals(passwd1)) return "redirect:/signup";

		String userid = req.getParameter("userid");
		String name = req.getParameter("name");
		String mobile = req.getParameter("mobile");
		String birthday = req.getParameter("birthday");
		String gender = req.getParameter("gender");
		String region = req.getParameter("region");
		String[] arInterest = req.getParameterValues("favorate");
		String favorate="";
		for( String x : arInterest ) {
			if(!favorate.equals("")) favorate+=",";
			favorate+=x;
		}
		mdao.insert(userid, passwd1, name, birthday, gender, region, favorate, mobile);
		
		return "redirect:/login";
	}
	@GetMapping("/board")
	public String showBoard(HttpServletRequest req) {
		HttpSession s = req.getSession();
		String userid = (String)s.getAttribute("userid");
		if( userid==null || userid.equals("")) {
			return "login";
		} 
		return "board";
	}
	@GetMapping("/write")
	public String write(HttpServletRequest req, Model model) {
		HttpSession s= req.getSession();
		String userid= (String) s.getAttribute("userid");
		model.addAttribute("userid",userid);
		return "board/write";
	}
	@PostMapping("/save")
	public String save(HttpServletRequest req, Model model) {
		String title= req.getParameter("title");
		String writer = req.getParameter("writer");
		String content = req.getParameter("content");
		bdao.insert(title, writer, content);
		
		return "redirect:/";
	}
	
	@GetMapping("/view")
	public String view(HttpServletRequest req, Model model) {
		int id =Integer.parseInt(req.getParameter("id"));
		BoardDTO bdto = bdao.getView(id);
		bdao.addHit(id);
		model.addAttribute("board",bdto);
		return "board/view";
	}
	@PostMapping("/saveBoard")
	   @ResponseBody
	   public String save(HttpServletRequest req) {
	      HttpSession s = req.getSession();
	      String writer = (String) s.getAttribute("userid");
	      String title = req.getParameter("title");
	      String content = req.getParameter("content");
	      bdao.insert(title, writer, content);
	      return "ok";
	   }
	@PostMapping("/updateBoard")
	@ResponseBody
	public String updateBoard(HttpServletRequest req) {
		int id = Integer.parseInt(req.getParameter("id"));
		  String title = req.getParameter("title");
		  String content = req.getParameter("content");
		  bdao.update(id, title , content);
		  return "ok";
	}
	@PostMapping("/viewBoard")
	@ResponseBody
	public String viewBoard(HttpServletRequest req) {
		int id =Integer.parseInt(req.getParameter("id"));
		BoardDTO bdto=bdao.getView(id);
		return bdto.getContent();
	}
	
	@PostMapping("/deleteBoard")
	@ResponseBody
	public String deleteBoard(HttpServletRequest req) {
		HttpSession s = req.getSession();
		String userid =(String) s.getAttribute("userid");
		if(userid ==null|| userid.equals("")) {
			return "";
		}
		int id= Integer.parseInt(req.getParameter("id"));
		bdao.delete(id);
		return "ok";
	}	
	@GetMapping("/delete")
	public String delete(HttpServletRequest req) {
		HttpSession s = req.getSession();
		String userid =(String) s.getAttribute("userid");
		if(userid ==null|| userid.equals("")){
			return "redirect:/";
		}
	    int id = Integer.parseInt(req.getParameter("id"));
	    BoardDTO bdto = bdao.getView(id);
	    if(userid.equals(bdto.getWriter())){
	    	   bdao.delete(id);
	    }
	    return "redirect:/";
	 }
	@GetMapping("/update")
	public String update(HttpServletRequest req, Model model) {
		HttpSession s = req.getSession();
		String userid =(String) s.getAttribute("userid");
		if(userid ==null|| userid.equals("")){
			return "redirect:/";
		}
	    int id = Integer.parseInt(req.getParameter("id"));
		BoardDTO bdto = bdao.getView(id);
	    if(userid.equals(bdto.getWriter())){
	    	model.addAttribute("board",bdto);
			return "board/update";
	    }else {
	    	return "redirect:/";
	    }
	
	}
	@PostMapping("/modify")
	public String modify(HttpServletRequest req, Model model) {
		int id =Integer.parseInt(req.getParameter("id"));
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		bdao.update(id,title,content);
		return "redirect:/";
	}
}